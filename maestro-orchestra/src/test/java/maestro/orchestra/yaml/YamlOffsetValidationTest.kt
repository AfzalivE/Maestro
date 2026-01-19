package maestro.orchestra.yaml

import maestro.orchestra.error.SyntaxError
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class YamlOffsetValidationTest {

    @Test
    fun `offset should be rejected in tapOn command`(@TempDir tempDir: Path) {
        val yaml = """
            appId: com.example.app
            ---
            - tapOn:
                offset: "100, 50"
        """.trimIndent()

        val yamlFile = tempDir.resolve("test.yaml")
        Files.writeString(yamlFile, yaml)

        val exception = assertThrows<SyntaxError> {
            YamlCommandReader.readCommands(yamlFile)
        }

        // Check that the error message contains relevant information about offset validation
        val message = exception.message ?: ""
        assert(message.contains("offset", ignoreCase = true)) { "Expected error message to contain 'offset', but got: $message" }
        assert(message.contains("dragAndDrop", ignoreCase = true)) { "Expected error message to contain 'dragAndDrop', but got: $message" }
    }

    @Test
    fun `offset should be rejected in dragAndDrop from field`(@TempDir tempDir: Path) {
        val yaml = """
            appId: com.example.app
            ---
            - dragAndDrop:
                from:
                  offset: "100, 50"
                to:
                  id: "target"
        """.trimIndent()

        val yamlFile = tempDir.resolve("test.yaml")
        Files.writeString(yamlFile, yaml)

        val exception = assertThrows<SyntaxError> {
            YamlCommandReader.readCommands(yamlFile)
        }

        val message = exception.message ?: ""
        assert(message.contains("offset", ignoreCase = true)) { "Expected error message to contain 'offset', but got: $message" }
        assert(message.contains("from", ignoreCase = true)) { "Expected error message to contain 'from', but got: $message" }
    }

    @Test
    fun `offset should be allowed in dragAndDrop to field`(@TempDir tempDir: Path) {
        val yaml = """
            appId: com.example.app
            ---
            - dragAndDrop:
                from:
                  text: "Item"
                to:
                  offset: "100, 50"
        """.trimIndent()

        val yamlFile = tempDir.resolve("test.yaml")
        Files.writeString(yamlFile, yaml)

        // Should not throw
        val commands = YamlCommandReader.readCommands(yamlFile)
        assert(commands.isNotEmpty())
    }

    @Test
    fun `offset should be rejected in assert command`(@TempDir tempDir: Path) {
        val yaml = """
            appId: com.example.app
            ---
            - assertVisible:
                offset: "100, 50"
        """.trimIndent()

        val yamlFile = tempDir.resolve("test.yaml")
        Files.writeString(yamlFile, yaml)

        val exception = assertThrows<SyntaxError> {
            YamlCommandReader.readCommands(yamlFile)
        }

        val message = exception.message ?: ""
        assert(message.contains("offset", ignoreCase = true)) { "Expected error message to contain 'offset', but got: $message" }
        assert(message.contains("dragAndDrop", ignoreCase = true)) { "Expected error message to contain 'dragAndDrop', but got: $message" }
    }

    @Test
    fun `offset should be rejected in copyTextFrom command`(@TempDir tempDir: Path) {
        val yaml = """
            appId: com.example.app
            ---
            - copyTextFrom:
                offset: "100, 50"
        """.trimIndent()

        val yamlFile = tempDir.resolve("test.yaml")
        Files.writeString(yamlFile, yaml)

        val exception = assertThrows<SyntaxError> {
            YamlCommandReader.readCommands(yamlFile)
        }

        val message = exception.message ?: ""
        assert(message.contains("offset", ignoreCase = true)) { "Expected error message to contain 'offset', but got: $message" }
        assert(message.contains("dragAndDrop", ignoreCase = true)) { "Expected error message to contain 'dragAndDrop', but got: $message" }
    }
}
