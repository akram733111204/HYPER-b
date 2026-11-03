
package com.rukn.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class VoiceCommandsTest {

    @Test
    fun cannotCorrectDateToImpossibleDay() {

        // Original valid date: leap year
        val originalValues = mapOf(
            VoiceField.EXPIRY_DATE to "2028-02-29"
        )

        // Attempt to change the year to a non-leap year
        val parsed = VoiceCommandEngine.interpret(
            "غير سنة الانتهاء الى 2027"
        )

        // Verify that the voice command was recognized
        val expectedCommands = listOf(
            VoiceCommand(
                VoiceField.EXPIRY_DATE,
                VoiceAction.SET_YEAR,
                "2027"
            )
        )

        assertEquals(
            expectedCommands,
            parsed.commands,
            "Voice command parsing failed. " +
                "Warnings: ${parsed.warnings}"
        )

        // Apply the command to the existing values
        val result = VoiceCommandEngine.apply(
            originalValues,
            parsed
        )

        // February 29, 2027 is invalid.
        // The original valid date must remain unchanged.
        assertEquals(
            "2028-02-29",
            result.values[VoiceField.EXPIRY_DATE],
            "An impossible date change was accepted. " +
                "Actual values: ${result.values}"
        )

        // The application should report a warning
        assertTrue(
            result.warnings.isNotEmpty(),
            "Expected a warning for the invalid date. " +
                "Actual warnings: ${result.warnings}"
        )
    }
}
