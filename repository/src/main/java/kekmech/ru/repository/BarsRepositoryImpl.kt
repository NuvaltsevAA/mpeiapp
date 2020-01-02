package kekmech.ru.repository

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.google.gson.GsonBuilder
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.repositories.BarsRepository.Companion.BARS_URL
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.sdk.bars.parser.BarsParser
import org.jsoup.Jsoup

class BarsRepositoryImpl constructor(
    private val context: Context,
    private val baseKeyStore: BaseKeyStore
) : BarsRepository {

    override var currentAcademicDiscipline: AcademicDiscipline? = null
        set(value) {
            if (value != null) {
                field = value
                saveCurrentAcademicDiscipline(value)
            }
        }
        get() = if (field == null) loadCurrentAcademicDisciplineFromCache() else field

    private fun saveCurrentAcademicDiscipline(value: AcademicDiscipline) {
        val gson = GsonBuilder().create()
        sharedPreferences
            .edit()
            .putString("current_discipline", gson.toJson(value))
            .apply()
    }

    private fun loadCurrentAcademicDisciplineFromCache(): AcademicDiscipline? {
        val disciplineJson = sharedPreferences.getString("current_discipline", null)
        if (disciplineJson != null) {
            val gson = GsonBuilder().create()
            return try {
                val discipline = gson.fromJson(disciplineJson, AcademicDiscipline::class.java)
                discipline
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            return null
        }
    }

    private val sharedPreferences = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)

    override val isLoggedIn: Boolean
        get() = sharedPreferences.getString("user1", "")?.isNotEmpty() ?: false

    override suspend fun getScoreAsync(forceRefresh: Boolean): AcademicScore? {
        return if (!forceRefresh)
            loadFromCache() ?: loadFromRemote()
        else
            loadFromRemote()
    }

    private fun loadFromCache(): AcademicScore? {
        val scoreJson = sharedPreferences.getString("score", null)
        if (scoreJson != null) {
            val gson = GsonBuilder().create()
            return try {
                val score = gson.fromJson(scoreJson, AcademicScore::class.java)
                score
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            return null
        }

    }

    private fun loadFromRemote(): AcademicScore? {
        var crashReport = ""
        try {
            val mainPage = Jsoup.connect(BARS_URL)
                .get()
            val stoken = mainPage
                .select("input[name=SToken]")
                .`val`()
            val requestVirificationToken = mainPage
                .select("input[name=__RequestVerificationToken]")
                .`val`()
            println(stoken)
            println(requestVirificationToken)

            val response = Jsoup.connect(BARS_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .data("Password", getPassword())
                .data("Username", getUsername())
                .data("Remember", "false")
                .data("SToken", stoken)
                .data("__RequestVerificationToken", requestVirificationToken)
                .post()

            crashReport = response.html()
            val barsParser = BarsParser()
            val score = barsParser.parse(response)
            saveToCache(score)
            return score
        } catch (e: BarsParser.LoginException) {
            // do nothing
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            Crashlytics.log(1, "bars_parser", "ERROR while parsing: $crashReport")
            Crashlytics.logException(e)
            return null
        }
    }

    private fun saveToCache(score: AcademicScore) {
        val gson = GsonBuilder().create()
        sharedPreferences
            .edit()
            .putString("score", gson.toJson(score))
            .apply()
    }

    private fun getUsername(): String {
        val username = sharedPreferences
            .getString("user1", null) ?: throw RuntimeException("User is not logged in")
        return baseKeyStore.decrypt(username)
    }

    private fun getPassword(): String {
        val password = sharedPreferences
            .getString("user2", null) ?: throw RuntimeException("User is not logged in")
        return baseKeyStore.decrypt(password)
    }

    override fun saveUserSecrets(username: String, password: String) {
        sharedPreferences
            .edit()
            .putString("user1", baseKeyStore.encrypt(username))
            .putString("user2", baseKeyStore.encrypt(password))
            .apply()
    }

    override fun clearUserSecrets() {
        sharedPreferences
            .edit()
            .putString("user1", "")
            .putString("user2", "") // PerezhilovaYD uxi762e
            .putString("score", "")
            .apply()
    }
}