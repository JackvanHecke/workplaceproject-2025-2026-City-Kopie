// components/communicatie/CommunicationForm.tsx
import React, { useState } from "react";
import { FAQ } from "../../types";

type Props = {
  onSubmit: (data: FAQ) => Promise<void> | void;
  submitLabel?: string;
};

const CommunicationForm: React.FC<Props> = ({
  onSubmit,
  submitLabel = "Opslaan",
}) => {
  const [question, setQuestion] = useState("");
  const [answer, setAnswer] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);

    try {
      const payload: FAQ = {
        question,
        answer,
      };

      await onSubmit(payload);

      // clear form after successful submit
      setQuestion("");
      setAnswer("");
    } catch (err: any) {
      console.error(err);
      setError(err?.message || "Er is een fout opgetreden.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      data-cy="communication-form"
      className="space-y-4 max-w-2xl mx-auto bg-white rounded-xl shadow-md p-6"
    >
      <h2
        className="text-xl font-semibold mb-2"
        data-cy="communication-form-title"
      >
        Nieuwe FAQ toevoegen
      </h2>

      {error && (
        <div
          className="border border-red-400 bg-red-50 text-red-700 px-3 py-2 rounded"
          data-cy="communication-error-alert"
        >
          {error}
        </div>
      )}

      <div>
        <label
          htmlFor="question"
          className="block text-sm font-medium text-gray-700 mb-1"
        >
          Vraag
        </label>
        <input
          id="question"
          type="text"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          required
          className="w-full border border-gray-300 rounded px-3 py-2 focus:ring focus:ring-blue-200 focus:border-blue-400"
          data-cy="FAQ-question-input"
        />
      </div>

      <div>
        <label
          htmlFor="answer"
          className="block text-sm font-medium text-gray-700 mb-1"
        >
          Antwoord
        </label>
        <textarea
          id="answer"
          value={answer}
          onChange={(e) => setAnswer(e.target.value)}
          required
          rows={4}
          className="w-full border border-gray-300 rounded px-3 py-2 focus:ring focus:ring-blue-200 focus:border-blue-400"
          data-cy="FAQ-answer-input"
        />
      </div>

      <div className="pt-2 flex justify-end">
        <button
          type="submit"
          disabled={submitting}
          data-cy="communication-submit-button"
          className="inline-flex items-center justify-center px-5 py-2.5 rounded-full bg-darker-client-bg text-white text-sm font-medium shadow-sm hover:bg-blue-700 disabled:opacity-60 disabled:cursor-not-allowed transition"
        >
          {submitting ? "Bezig..." : submitLabel}
        </button>
      </div>
    </form>
  );
};

export default CommunicationForm;
