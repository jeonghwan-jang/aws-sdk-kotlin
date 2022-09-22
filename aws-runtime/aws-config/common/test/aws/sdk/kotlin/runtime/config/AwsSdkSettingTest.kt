package aws.sdk.kotlin.runtime.config

import aws.sdk.kotlin.runtime.config.retries.RetryMode
import aws.smithy.kotlin.runtime.util.PlatformEnvironProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AwsSdkSettingTest {

    @Test
    fun itResolvesJVMSettingFirst() {
        val testPlatform = mockPlatform(mapOf("AWS_PROFILE" to "env"), mapOf("aws.profile" to "jvm"))

        val actual = AwsSdkSetting.AwsProfile.resolve(testPlatform)

        assertEquals("jvm", actual)
    }

    @Test
    fun itResolvesEnvSettingSecond() {
        val testPlatform = mockPlatform(mapOf("AWS_PROFILE" to "env"), mapOf())

        val actual = AwsSdkSetting.AwsProfile.resolve(testPlatform)

        assertEquals("env", actual)
    }

    @Test
    fun itResolvesDefaultSettingThird() {
        val testPlatform = mockPlatform(mapOf(), mapOf())

        val actual = AwsSdkSetting.AwsProfile.resolve(testPlatform)

        assertEquals("default", actual)
    }

    @Test
    fun itReturnsNullWithNoValue() {
        val testPlatform = mockPlatform(mapOf(), mapOf())

        val actual = AwsSdkSetting.AwsAccessKeyId.resolve(testPlatform)

        assertNull(actual)
    }

    @Test
    fun itResolvesType() {
        val testPlatform = mockPlatform(mapOf("AWS_EC2_METADATA_DISABLED" to "true"), mapOf())

        val actual = AwsSdkSetting.AwsEc2MetadataDisabled.resolve(testPlatform)
        assertEquals(true, actual)
    }

    @Test
    fun itResolvesMaxAttemptsFromEnvironmentVariables() {
        val expected = 5
        val testPlatform = mockPlatform(mapOf("AWS_MAX_ATTEMPTS" to expected.toString()), mapOf())

        val actual = AwsSdkSetting.AwsMaxAttempts.resolve(testPlatform)
        assertEquals(expected, actual)
    }

    @Test
    fun itResolvesRetryModeFromSystemProperties() {
        val expected = RetryMode.LEGACY
        val testPlatform = mockPlatform(mapOf(), mapOf("aws.retryMode" to "legacy"))

        val actual = AwsSdkSetting.AwsRetryMode.resolve(testPlatform)
        assertEquals(expected, actual)
    }

    private fun mockPlatform(env: Map<String, String>, jvmProps: Map<String, String>): PlatformEnvironProvider {
        return object : PlatformEnvironProvider {
            override fun getAllEnvVars(): Map<String, String> = env
            override fun getenv(key: String): String? = env[key]
            override fun getAllProperties(): Map<String, String> = jvmProps
            override fun getProperty(key: String): String? = jvmProps[key]
        }
    }
}
