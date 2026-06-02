import { Note } from "@types";

const API_URL = '/api';

const NoteService = {
  async getAllNotesForLocation(locationId: number): Promise<Note[]> {
    const response = await fetch(`/api/notes?locationId=${locationId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Failed to fetch notes');
    }

    return response.json();
  },

  async createNote(locationId: number, content: string): Promise<Note> {
    const response = await fetch(`/api/notes`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ locationId, content }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Failed to create note');
    }

    return response.json();
  },

  async updateNote(noteId: number, content: string): Promise<Note> {
    const response = await fetch(`/api/notes/${noteId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ content }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Failed to update note');
    }

    return response.json();
  },

  async deleteNote(noteId: number): Promise<void> {
    const response = await fetch(`/api/notes/${noteId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error || 'Failed to delete note');
    }
  },
};

export default NoteService;
