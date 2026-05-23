package com.tsdc.vinilos

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tsdc.vinilos.domain.models.Album
import com.tsdc.vinilos.domain.models.Track
import com.tsdc.vinilos.domain.repositories.AlbumRepository
import com.tsdc.vinilos.domain.usecases.AddTrackToAlbumUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumTracksUseCase
import com.tsdc.vinilos.ui.screens.AlbumTracksScreen
import com.tsdc.vinilos.ui.shared.constants.UiTestTags
import com.tsdc.vinilos.ui.shared.theme.VinilosTheme
import com.tsdc.vinilos.ui.viewmodels.AlbumTracksViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumTracksComposeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val sampleAlbum = Album(
        id = 3,
        name = "A Night at the Opera",
        cover = "",
        releaseDate = "1975-11-21",
        description = "",
        genre = "Rock",
        recordLabel = "EMI"
    )

    private val initialTracks = mapOf(
        sampleAlbum.id to listOf(
            Track(id = 1, name = "Bohemian Rhapsody", duration = "5:55"),
            Track(id = 2, name = "You're My Best Friend", duration = "2:52")
        )
    )

    private var backCalls = 0
    private var saveCalls = 0
    private var bottomNavIndex = -1

    private fun launchAlbumTracks(
        albumId: Int = sampleAlbum.id,
        repository: AlbumRepository = FakeAlbumRepository(initialTracks = initialTracks)
    ) {
        backCalls = 0
        saveCalls = 0
        bottomNavIndex = -1
        composeRule.setContent {
            VinilosTheme {
                val viewModel = AlbumTracksViewModel(
                    GetAlbumTracksUseCase(repository),
                    AddTrackToAlbumUseCase(repository),
                    GetAlbumByIdUseCase(repository)
                )
                AlbumTracksScreen(
                    viewModel = viewModel,
                    albumId = albumId,
                    onBack = { backCalls += 1 },
                    onSaveTracklist = { saveCalls += 1 },
                    onBottomNavSelected = { bottomNavIndex = it }
                )
            }
        }
    }

    private fun waitForText(text: String, timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule.onAllNodesWithText(text, substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun waitForTracksItems(timeoutMs: Long = 10_000L) {
        composeRule.waitUntil(timeoutMs) {
            composeRule
                .onAllNodesWithTag(UiTestTags.ALBUM_TRACKS_LIST_ITEM, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun albumTracks_showsHeaderAndStaticElements() {
        launchAlbumTracks()
        waitForText(sampleAlbum.name)

        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_ROOT).assertIsDisplayed()
        composeRule.onNodeWithText("Albums").assertIsDisplayed()
        composeRule.onNodeWithText("EDITING").assertIsDisplayed()
        composeRule.onNodeWithText(sampleAlbum.name).assertIsDisplayed()
        composeRule.onNodeWithText("TRACKLIST").assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_TITLE_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_DURATION_FIELD).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_ADD_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_SAVE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun albumTracks_loadsExistingTracksFromRepository() {
        launchAlbumTracks()
        waitForTracksItems()

        composeRule.onNodeWithText("Bohemian Rhapsody").assertIsDisplayed()
        composeRule.onNodeWithText("5:55").assertIsDisplayed()
        composeRule.onNodeWithText("You're My Best Friend").assertIsDisplayed()
        composeRule.onNodeWithText("2:52").assertIsDisplayed()
    }

    @Test
    fun albumTracks_addTrack_appendsToList() {
        launchAlbumTracks()
        waitForTracksItems()

        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_TITLE_FIELD).performTextInput("'39")
        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_DURATION_FIELD).performTextInput("3:30")
        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_ADD_BUTTON).performClick()

        waitForText("'39")
        composeRule.onNodeWithText("'39").assertIsDisplayed()
        composeRule.onNodeWithText("3:30").assertIsDisplayed()
    }

    @Test
    fun albumTracks_addButton_doesNothingWhenFieldsEmpty() {
        launchAlbumTracks(repository = FakeAlbumRepository(initialTracks = emptyMap()))
        waitForText("Aún no hay tracks")

        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_ADD_BUTTON).performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Aún no hay tracks", substring = true).assertIsDisplayed()
    }

    @Test
    fun albumTracks_saveButton_invokesCallback() {
        launchAlbumTracks()
        waitForTracksItems()

        composeRule.onNodeWithTag(UiTestTags.ALBUM_TRACKS_SAVE_BUTTON).performClick()
        composeRule.waitForIdle()

        assertEquals(1, saveCalls)
    }

    @Test
    fun albumTracks_backButton_invokesCallback() {
        launchAlbumTracks()
        waitForText(sampleAlbum.name)

        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.waitForIdle()

        assertEquals(1, backCalls)
    }

    @Test
    fun albumTracks_bottomNav_invokesCallbackWithIndex() {
        launchAlbumTracks()
        waitForTracksItems()

        composeRule.onNodeWithText("HOME").performClick()
        composeRule.waitForIdle()

        assertEquals(0, bottomNavIndex)
    }

    @Test
    fun albumTracks_emptyAlbum_showsEmptyMessage() {
        launchAlbumTracks(
            albumId = sampleAlbum.id,
            repository = FakeAlbumRepository(initialTracks = emptyMap())
        )
        waitForText("Aún no hay tracks")

        composeRule.onNodeWithText("Aún no hay tracks", substring = true).assertIsDisplayed()
    }
}
