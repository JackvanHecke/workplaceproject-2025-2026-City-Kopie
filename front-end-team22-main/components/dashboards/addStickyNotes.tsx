import { useState, useEffect } from 'react';
import { Note } from '@types';
import NoteService from '@services/NoteService';

type Props = {
  locationId: number;
};

const addStickyNotes: React.FC<Props> = ({ locationId }) => {
  const [notes, setNotes] = useState<Note[]>([]);
  const [newNoteContent, setNewNoteContent] = useState('');
  const [editingNoteId, setEditingNoteId] = useState<number | null>(null);
  const [editContent, setEditContent] = useState('');
  const [isAddingNew, setIsAddingNew] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadNotes();
  }, [locationId]);

  const loadNotes = async () => {
    try {
      setLoading(true);
      const fetchedNotes = await NoteService.getAllNotesForLocation(locationId);
      setNotes(fetchedNotes);
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to load notes');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateNote = async () => {
    if (!newNoteContent.trim()) {
      setError('Note content cannot be empty');
      return;
    }

    try {
      setLoading(true);
      await NoteService.createNote(locationId, newNoteContent);
      setNewNoteContent('');
      setIsAddingNew(false);
      await loadNotes();
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to create note');
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateNote = async (noteId: number) => {
    if (!editContent.trim()) {
      setError('Note content cannot be empty');
      return;
    }

    try {
      setLoading(true);
      await NoteService.updateNote(noteId, editContent);
      setEditingNoteId(null);
      setEditContent('');
      await loadNotes();
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to update note');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteNote = async (noteId: number) => {
    if (!confirm('Weet je zeker dat je deze notitie wilt verwijderen?')) {
      return;
    }

    try {
      setLoading(true);
      await NoteService.deleteNote(noteId);
      await loadNotes();
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to delete note');
    } finally {
      setLoading(false);
    }
  };

  const startEditing = (note: Note) => {
    setEditingNoteId(note.id);
    setEditContent(note.content);
  };

  const cancelEditing = () => {
    setEditingNoteId(null);
    setEditContent('');
  };

  return (
    <div className="sticky-notes-container">
      <div className="sticky-notes-header">
        <h2>Mijn Notities</h2>
        {!isAddingNew && (
          <button
            className="btn-add-note"
            onClick={() => setIsAddingNew(true)}
            disabled={loading}
          >
            + Nieuwe Notitie
          </button>
        )}
      </div>

      {error && (
        <div className="error-message">
          ⚠️ {error}
        </div>
      )}

      {isAddingNew && (
        <div className="sticky-note new-note">
          <textarea
            className="note-textarea"
            placeholder="Typ hier je notitie..."
            value={newNoteContent}
            onChange={(e) => setNewNoteContent(e.target.value)}
            rows={6}
            autoFocus
          />
          <div className="note-actions">
            <button
              className="btn-save"
              onClick={handleCreateNote}
              disabled={loading}
            >
              💾 Opslaan
            </button>
            <button
              className="btn-cancel"
              onClick={() => {
                setIsAddingNew(false);
                setNewNoteContent('');
              }}
              disabled={loading}
            >
              ✖️ Annuleren
            </button>
          </div>
        </div>
      )}

      {loading && <div className="loading">Laden...</div>}

      <div className="notes-grid">
        {notes.map((note) => (
          <div key={note.id} className="sticky-note">
            {editingNoteId === note.id ? (
              <>
                <textarea
                  className="note-textarea"
                  value={editContent}
                  onChange={(e) => setEditContent(e.target.value)}
                  rows={6}
                  autoFocus
                />
                <div className="note-actions">
                  <button
                    className="btn-save"
                    onClick={() => handleUpdateNote(note.id)}
                    disabled={loading}
                  >
                    💾 Opslaan
                  </button>
                  <button
                    className="btn-cancel"
                    onClick={cancelEditing}
                    disabled={loading}
                  >
                    ✖️ Annuleren
                  </button>
                </div>
              </>
            ) : (
              <>
                <div className="note-content">{note.content}</div>
                <div className="note-footer">
                  <small className="note-date">
                    {new Date(note.updatedAt).toLocaleDateString('nl-NL', {
                      day: '2-digit',
                      month: '2-digit',
                      year: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit',
                    })}
                  </small>
                  <div className="note-actions">
                    <button
                      className="btn-edit"
                      onClick={() => startEditing(note)}
                      disabled={loading}
                      title="Bewerken"
                    >
                      ✏️
                    </button>
                    <button
                      className="btn-delete"
                      onClick={() => handleDeleteNote(note.id)}
                      disabled={loading}
                      title="Verwijderen"
                    >
                      🗑️
                    </button>
                  </div>
                </div>
              </>
            )}
          </div>
        ))}
      </div>

      {notes.length === 0 && !isAddingNew && !loading && (
        <div className="empty-state">
          <p>Geen notities gevonden. Voeg je eerste notitie toe!</p>
        </div>
      )}

      <style jsx>{`
        .sticky-notes-container {
          padding: 20px;
          max-width: 1200px;
          margin: 0 auto;
        }

        .sticky-notes-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 24px;
        }

        .sticky-notes-header h2 {
          font-size: 28px;
          font-weight: bold;
          color: #333;
          margin: 0;
        }

        .btn-add-note {
          background-color: #10b981;
          color: white;
          border: none;
          padding: 12px 24px;
          border-radius: 8px;
          font-size: 16px;
          font-weight: 600;
          cursor: pointer;
          transition: background-color 0.2s;
        }

        .btn-add-note:hover:not(:disabled) {
          background-color: #059669;
        }

        .btn-add-note:disabled {
          opacity: 0.6;
          cursor: not-allowed;
        }

        .error-message {
          background-color: #fee;
          border: 1px solid #fcc;
          color: #c33;
          padding: 12px 16px;
          border-radius: 8px;
          margin-bottom: 16px;
          font-size: 14px;
        }

        .loading {
          text-align: center;
          padding: 20px;
          color: #666;
          font-size: 16px;
        }

        .notes-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
          gap: 24px;
          margin-top: 24px;
        }

        .sticky-note {
          background: linear-gradient(135deg, #fef08a 0%, #fde047 100%);
          border-radius: 8px;
          padding: 20px;
          box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 2px 4px rgba(0, 0, 0, 0.06);
          min-height: 200px;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          transition: transform 0.2s, box-shadow 0.2s;
          position: relative;
        }

        .sticky-note:hover {
          transform: translateY(-4px);
          box-shadow: 0 8px 12px rgba(0, 0, 0, 0.15), 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .new-note {
          background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
          grid-column: 1 / -1;
          max-width: 600px;
        }

        .note-content {
          flex: 1;
          font-size: 15px;
          line-height: 1.6;
          color: #1f2937;
          white-space: pre-wrap;
          word-wrap: break-word;
          margin-bottom: 12px;
        }

        .note-textarea {
          width: 100%;
          padding: 12px;
          border: 2px solid #cbd5e1;
          border-radius: 6px;
          font-size: 15px;
          line-height: 1.6;
          resize: vertical;
          font-family: inherit;
          background-color: rgba(255, 255, 255, 0.9);
          margin-bottom: 12px;
        }

        .note-textarea:focus {
          outline: none;
          border-color: #3b82f6;
          box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }

        .note-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-top: auto;
          padding-top: 12px;
          border-top: 1px solid rgba(0, 0, 0, 0.1);
        }

        .note-date {
          color: #64748b;
          font-size: 12px;
        }

        .note-actions {
          display: flex;
          gap: 8px;
        }

        .btn-edit,
        .btn-delete,
        .btn-save,
        .btn-cancel {
          border: none;
          background: transparent;
          cursor: pointer;
          font-size: 20px;
          padding: 4px 8px;
          border-radius: 4px;
          transition: background-color 0.2s;
        }

        .btn-save,
        .btn-cancel {
          background-color: white;
          padding: 8px 16px;
          font-size: 14px;
          font-weight: 600;
        }

        .btn-save {
          color: #10b981;
        }

        .btn-cancel {
          color: #ef4444;
        }

        .btn-edit:hover:not(:disabled),
        .btn-delete:hover:not(:disabled),
        .btn-save:hover:not(:disabled),
        .btn-cancel:hover:not(:disabled) {
          background-color: rgba(255, 255, 255, 0.8);
        }

        .btn-edit:disabled,
        .btn-delete:disabled,
        .btn-save:disabled,
        .btn-cancel:disabled {
          opacity: 0.5;
          cursor: not-allowed;
        }

        .empty-state {
          text-align: center;
          padding: 60px 20px;
          color: #9ca3af;
          font-size: 16px;
        }

        @media (max-width: 768px) {
          .notes-grid {
            grid-template-columns: 1fr;
          }
        }
      `}</style>
    </div>
  );
}

export default addStickyNotes;