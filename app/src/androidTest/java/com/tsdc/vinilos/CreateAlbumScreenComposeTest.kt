package com.tsdc.vinilos

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsdc.vinilos.domain.usecases.CreateAlbumUseCase
import com.tsdc.vinilos.ui.screens.CreateAlbumScreen
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.CreateAlbumViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateAlbumScreenComposeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun buildViewModel(): CreateAlbumViewModel =
        CreateAlbumViewModel(CreateAlbumUseCase(FakeAlbumRepository()))

    @Test
    fun createAlbumScreen_showsMainElements() {
        composeRule.setContent {
            VinilosTheme {
                CreateAlbumScreen(
                    viewModel = buildViewModel(),
                    onBack = {},
                    onBottomNavSelected = {}
                )
            }
        }

        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_ROOT).assertIsDisplayed()
        composeRule.onNodeWithText("Create Album").assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_TITLE_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_DATE_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_COVER_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_GENRE_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_LABEL_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_DESCRIPTION_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_SUBMIT_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_SAVE_DRAFT_BUTTON).assertIsDisplayed()
    }

    @Test
    fun createAlbumScreen_allowsTypingInEditableFields() {
        composeRule.setContent {
            VinilosTheme {
                CreateAlbumScreen(
                    viewModel = buildViewModel(),
                    onBack = {},
                    onBottomNavSelected = {}
                )
            }
        }

        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_TITLE_FIELD).performTextInput("A Night at the Opera")
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_COVER_FIELD).performTextInput("https://img.test/queen.jpg")
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_DESCRIPTION_FIELD).performTextInput("Classic album")

        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_TITLE_FIELD).assertTextContains("A Night at the Opera")
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_COVER_FIELD).assertTextContains("https://img.test/queen.jpg")
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_DESCRIPTION_FIELD).assertTextContains("Classic album")
    }

    @Test
    fun createAlbumScreen_genreAndDate_canBeSelected() {
        composeRule.setContent {
            VinilosTheme {
                CreateAlbumScreen(
                    viewModel = buildViewModel(),
                    onBack = {},
                    onBottomNavSelected = {}
                )
            }
        }

        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_GENRE_FIELD).performClick()
        composeRule.onNodeWithText("Rock").performClick()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_GENRE_FIELD).assertTextContains("Rock")

        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_DATE_FIELD).performClick()
        composeRule.onNodeWithText("OK").assertIsDisplayed()
        composeRule.onNodeWithText("OK").performClick()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_DATE_FIELD).assertTextContains("/")
    }

    @Test
    fun createAlbumScreen_clickActions_invokesCallbacks() {
        var backClicks = 0
        var saveDraftClicks = 0
        var selectedBottomIndex = -1

        composeRule.setContent {
            VinilosTheme {
                CreateAlbumScreen(
                    viewModel = buildViewModel(),
                    onBack = { backClicks++ },
                    onBottomNavSelected = { selectedBottomIndex = it },
                    onSaveDraft = { saveDraftClicks++ }
                )
            }
        }

        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_BACK_BUTTON).performClick()
        composeRule.onNodeWithTag(UiTestTags.CREATE_ALBUM_SAVE_DRAFT_BUTTON).performClick()
        composeRule.onNodeWithText("ARTISTS").performClick()
        composeRule.waitForIdle()

        assertEquals(1, backClicks)
        assertEquals(1, saveDraftClicks)
        assertEquals(2, selectedBottomIndex)
    }
}
