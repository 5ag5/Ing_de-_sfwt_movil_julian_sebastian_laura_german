package com.tsdc.vinilos

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsdc.vinilos.domain.usecases.GetArtistByIdUseCase
import com.tsdc.vinilos.domain.usecases.IsFavoriteArtistUseCase
import com.tsdc.vinilos.domain.usecases.ToggleFavoriteArtistUseCase
import com.tsdc.vinilos.ui.screens.ArtistDetailScreen
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.ArtistDetailViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistDetailComposeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private var backCalls = 0

    private fun launchArtistDetail(artistId: Int = 100) {
        backCalls = 0
        composeRule.setContent {
            VinilosTheme {
                val repository = FakeArtistRepository()
                val viewModel = ArtistDetailViewModel(
                    GetArtistByIdUseCase(repository),
                    ToggleFavoriteArtistUseCase(repository),
                    IsFavoriteArtistUseCase(repository)
                )
                ArtistDetailScreen(
                    viewModel = viewModel,
                    artistId = artistId,
                    onBack = { backCalls += 1 }
                )
            }
        }
    }

    private fun waitForArtistName(name: String, timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule.onAllNodesWithText(name, substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun waitForText(text: String, timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitForTextAbsent(text: String, timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isEmpty()
        }
    }

    @Test
    fun artistDetail_showsArtistDataAndMetadata() {
        launchArtistDetail()
        waitForArtistName("Rubén Blades")

        composeRule.onNodeWithTag(UiTestTags.ARTIST_DETAIL_ROOT).assertIsDisplayed()
        composeRule.onNodeWithText("Artists").assertIsDisplayed()
        composeRule.onNodeWithText("FEATURED ARTIST").assertIsDisplayed()
        composeRule.onNodeWithText("METADATA").assertIsDisplayed()
        composeRule.onNodeWithText("ID").assertIsDisplayed()
        composeRule.onNodeWithText("Fecha de nacimiento").assertIsDisplayed()
        composeRule.onNodeWithText("View Albums").assertIsDisplayed()
        composeRule.onNodeWithText("Follow Artist").assertIsDisplayed()
    }

    @Test
    fun artistDetail_tapHeartIcon_togglesFollowState() {
        launchArtistDetail()
        waitForArtistName("Rubén Blades")

        composeRule.onNodeWithText("Follow Artist").assertIsDisplayed()

        composeRule.onNodeWithTag(UiTestTags.ARTIST_DETAIL_FAVORITE).performClick()
        waitForText("Following Artist")

        composeRule.onNodeWithTag(UiTestTags.ARTIST_DETAIL_FAVORITE).performClick()
        waitForText("Follow Artist")
        waitForTextAbsent("Following Artist")
    }

    @Test
    fun artistDetail_tapFollowButton_addsToFavorites() {
        launchArtistDetail()
        waitForArtistName("Rubén Blades")

        composeRule.onNodeWithTag(UiTestTags.ARTIST_DETAIL_FOLLOW).performClick()
        waitForText("Following Artist")

        composeRule.onNodeWithContentDescription("Quitar de favoritos").assertIsDisplayed()
    }

    @Test
    fun artistDetail_tapBack_invokesOnBack() {
        launchArtistDetail()
        waitForArtistName("Rubén Blades")

        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.waitForIdle()

        assertEquals(1, backCalls)
    }

    @Test
    fun artistDetail_loadsRequestedArtist_byId() {
        launchArtistDetail(artistId = 101)
        waitForArtistName("Queen")

        composeRule.onNodeWithText("Queen").assertIsDisplayed()
    }
}
