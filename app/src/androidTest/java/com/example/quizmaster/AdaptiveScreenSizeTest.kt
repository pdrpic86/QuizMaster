package com.example.quizmaster

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.then
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizmaster.ui.util.AdaptiveDensity
import com.example.quizmaster.ui.util.rememberAdaptiveScale
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalTestApi::class)
@RunWith(Parameterized::class)
class AdaptiveScreenSizeTest(
    private val spec: ScreenSpec
) {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun adaptiveScale_isSaneForPopularPhoneSize() {
        var measuredScale = -1f

        composeRule.setContent {
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(
                    DpSize(spec.widthDp.dp, spec.heightDp.dp)
                ) then DeviceConfigurationOverride.FontScale(spec.fontScale)
            ) {
                val adaptive = rememberAdaptiveScale()

                SideEffect {
                    measuredScale = adaptive.scale
                }
            }
        }

        composeRule.runOnIdle {
            assertTrue(
                spec.name + " scale too small: " + measuredScale,
                measuredScale >= spec.minExpectedScale
            )

            assertTrue(
                spec.name + " scale too large: " + measuredScale,
                measuredScale <= spec.maxExpectedScale
            )
        }
    }

    @Test
    fun longQuestionAndFourAnswers_doNotFallOutsideScreen() {
        composeRule.setContent {
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(
                    DpSize(spec.widthDp.dp, spec.heightDp.dp)
                ) then DeviceConfigurationOverride.FontScale(spec.fontScale)
            ) {
                AdaptiveDensity {
                    QuestionFitProbe()
                }
            }
        }

        composeRule.onNodeWithTag("probe-root", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("question-card", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("answer-1", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("answer-4", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        assertInsideRoot("question-card")
        assertInsideRoot("answers-column")
        assertInsideRoot("answer-4")
    }

    private fun assertInsideRoot(tag: String) {
        val root = composeRule
            .onRoot(useUnmergedTree = true)
            .getUnclippedBoundsInRoot()

        val node = composeRule
            .onNodeWithTag(tag, useUnmergedTree = true)
            .assertExists()
            .getUnclippedBoundsInRoot()

        assertBoundsInsideRoot(tag, node, root)
    }

    private fun assertBoundsInsideRoot(
        tag: String,
        node: DpRect,
        root: DpRect
    ) {
        val tolerance = 1.dp

        assertTrue(
            spec.name + " " + tag + " left clipped. node=" + node + " root=" + root,
            node.left >= root.left - tolerance
        )

        assertTrue(
            spec.name + " " + tag + " top clipped. node=" + node + " root=" + root,
            node.top >= root.top - tolerance
        )

        assertTrue(
            spec.name + " " + tag + " right clipped. node=" + node + " root=" + root,
            node.right <= root.right + tolerance
        )

        assertTrue(
            spec.name + " " + tag + " bottom clipped. node=" + node + " root=" + root,
            node.bottom <= root.bottom + tolerance
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun devices(): List<ScreenSpec> = listOf(
            ScreenSpec(
                name = "small_360x640",
                widthDp = 360,
                heightDp = 640,
                fontScale = 1.0f,
                minExpectedScale = 0.60f,
                maxExpectedScale = 0.66f
            ),
            ScreenSpec(
                name = "compact_360x780",
                widthDp = 360,
                heightDp = 780,
                fontScale = 1.0f,
                minExpectedScale = 0.78f,
                maxExpectedScale = 0.85f
            ),
            ScreenSpec(
                name = "xiaomi_like_393x851",
                widthDp = 393,
                heightDp = 851,
                fontScale = 1.0f,
                minExpectedScale = 0.90f,
                maxExpectedScale = 0.96f
            ),
            ScreenSpec(
                name = "pixel_like_411x891",
                widthDp = 411,
                heightDp = 891,
                fontScale = 1.0f,
                minExpectedScale = 0.94f,
                maxExpectedScale = 1.00f
            ),
            ScreenSpec(
                name = "samsung_like_412x915",
                widthDp = 412,
                heightDp = 915,
                fontScale = 1.0f,
                minExpectedScale = 0.96f,
                maxExpectedScale = 1.03f
            ),
            ScreenSpec(
                name = "large_phone_430x932",
                widthDp = 430,
                heightDp = 932,
                fontScale = 1.0f,
                minExpectedScale = 0.98f,
                maxExpectedScale = 1.07f
            ),
            ScreenSpec(
                name = "xiaomi_like_393x851_font_125",
                widthDp = 393,
                heightDp = 851,
                fontScale = 1.25f,
                minExpectedScale = 0.90f,
                maxExpectedScale = 0.96f
            )
        )
    }
}

data class ScreenSpec(
    val name: String,
    val widthDp: Int,
    val heightDp: Int,
    val fontScale: Float,
    val minExpectedScale: Float,
    val maxExpectedScale: Float
) {
    override fun toString(): String = name
}

@Composable
private fun QuestionFitProbe() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("probe-root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Question 7 / 10",
                modifier = Modifier.testTag("question-header"),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )

            LinearProgressIndicator(
                progress = { 0.62f },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("timer-progress")
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("question-card"),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = LONG_QUESTION,
                        modifier = Modifier.testTag("question-text"),
                        fontSize = 18.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("answers-column"),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ANSWERS.forEachIndexed { index, answer ->
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 42.dp)
                            .testTag("answer-" + (index + 1)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = answer,
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

private const val LONG_QUESTION =
    "Which famous historical event caused a major political crisis, changed alliances across Europe, and is often used as an example of how one incident can trigger a much larger conflict?"

private val ANSWERS = listOf(
    "The assassination of Archduke Franz Ferdinand",
    "The signing of the Treaty of Versailles",
    "The fall of the Berlin Wall",
    "The discovery of penicillin by Alexander Fleming"
)
