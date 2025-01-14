package co.dhan.api.ondemand;

import co.dhan.api.DhanContext;
import co.dhan.constant.Exchange;
import co.dhan.constant.Segment;
import co.dhan.http.DhanAPIException;
import co.dhan.http.DhanResponse;
import co.dhan.dto.EDISStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SecurityEndpoint {

    interface APIParam {
        String OTPSent = "OTP sent";
        String ISIN = "isin";
        String Quantity = "qty";
        String Exchange = "exchange";
        String Segment = "segment";
        String Bulk = "bulk";
        String EdisFormHtml = "edisFormHtml";
    }

    interface APIEndopint {
        String TempWebFormPrefix = "dhan_tpin_";
        String TempWebFormSufix = ".html";
        String EdisTPin = "/edis/tpin";
        String EdisForm = "/edis/form";
        String EdisInquire = "/edis/inquire/%s";
    }

    private final DhanContext dhanContext;

    public SecurityEndpoint(DhanContext dhanContext) {
        this.dhanContext = dhanContext;
    }

    public EDISStatus getEDISStatusOf(String isin) throws DhanAPIException {
        String endpoint = String.format(APIEndopint.EdisInquire, isin);
        return dhanContext.getDhanHTTP()
                .doHttpGetRequest(endpoint)
                .convertToType(EDISStatus.class);
    }

    public String generateTPIN() throws DhanAPIException {
        dhanContext.getDhanHTTP().doHttpGetRequest(APIEndopint.EdisTPin);
        return APIParam.OTPSent;
    }

    public void openBrowserForTPin(String isin, int quantity,
                                           Exchange exchange, Segment segment, boolean bulk)
            throws DhanAPIException, IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put(APIParam.ISIN, isin);
        payload.put(APIParam.Quantity, String.valueOf(quantity));
        payload.put(APIParam.Exchange, String.valueOf(exchange));
        payload.put(APIParam.Segment, String.valueOf(segment));
        payload.put(APIParam.Bulk, String.valueOf(bulk));

        DhanResponse dhanResponse = dhanContext.getDhanHTTP()
                .doHttpPostRequest(APIEndopint.EdisForm, payload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> map = objectMapper.readValue(dhanResponse.toString(), Map.class);
            String formHtml = map.get(APIParam.EdisFormHtml);
            // Remove any backslashes (\) from the HTML
            formHtml = formHtml.replaceAll("\\\\", "");
            // Save HTML to temporary file and open in browser
            File tempFile = createTempHtmlFile(formHtml);
            openInBrowser(tempFile);
        } catch (JsonProcessingException e) {
            String msg = String.format("Error processing HTTP Response: %s",dhanResponse.toString());
            log.error(msg);
            throw new RuntimeException(e);
        }
    }

    private File createTempHtmlFile(String formHtml) throws IOException {
        byte[] bytes = formHtml.getBytes();
        File tempFile = File.createTempFile(APIEndopint.TempWebFormPrefix, APIEndopint.TempWebFormSufix);
        Files.write(tempFile.toPath(), bytes);
        return tempFile;
    }

    private void openInBrowser(File tempFile) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(tempFile.toURI());
            } catch (IllegalArgumentException e) {
                String msg = "Error opening file: " + e.getMessage();
                log.error(msg);
                throw new IOException(msg);
            }
        } else {
            String msg = String.format("Desktop is not supported. Cannot open browser automatically. " +
                    "Please open the following file manually: %s", tempFile.getAbsolutePath());
            log.error(msg);
            throw new IOException(msg);
        }
    }
}
