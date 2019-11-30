package kekmech.ru.core.repositories

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time

interface NotesRepository {
    var noteCreationTransaction: CoupleNative?

    fun getNoteFor(scheduleId: Int, dayOfWeek: Int, weekNum: Int, coupleNum: Int): NoteNative?
    fun getNoteFor(time: Time): NoteNative?
    fun getNoteDyId(id: Int): NoteNative?
    fun saveNote(note: NoteNative, isNoteEmpty: Boolean = false)
    fun removeNote(note: NoteNative)
}