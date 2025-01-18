package co.dhan;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
public abstract class UnitTestRoot {
    @NotNull
    protected String getExpectedResponseFromResource(String filepath) throws IOException, URISyntaxException {
        return Files.readString(Paths.get(getClass().getResource(filepath).toURI()));
    }
}
