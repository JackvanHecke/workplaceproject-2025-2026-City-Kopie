// pages/communicatie/index.tsx
import Head from "next/head";
import { useCallback, useEffect, useState } from "react";
import { useRouter } from "next/router";

import Header from "@components/header";
import SideNav from "@components/sideNav";

import CommunicationService from "../../services/CommunicationService";
import { CommunicationMessageDTO } from "../../types";
import { useUser } from "utils/UserContext";

const Communicatie: React.FC = () => {
  const router = useRouter();
  const { messageId } = router.query;
  const [messages, setMessages] = useState<CommunicationMessageDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedMessage, setSelectedMessage] =
    useState<CommunicationMessageDTO | null>(null);
  const { currentUser, loading: userLoading } = useUser();

  const loadData = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await CommunicationService.getAll();
      if (!res.ok)
        throw new Error(`Failed to fetch communications (${res.status})`);
      const data = await res.json();
      setMessages(data);
    } catch (err: any) {
      console.error(err);
      setError(err?.message || "Kon communicatie niet laden.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (!userLoading && !currentUser) {
      router.push("/login");
      return;
    }
    if (
      currentUser &&
      ["END_USER" ].includes(currentUser.role)
    ) {
      router.push("/");
      return;
    }

    const openFromQuery = async () => {
      if (!messageId) return;
      const numericId = Number(messageId);
      if (!Number.isFinite(numericId)) return;

      try {
        const res = await CommunicationService.getById(numericId);
        if (!res.ok) return;
        const data = await res.json();
        setSelectedMessage(data);
        setModalOpen(true);
      } catch (e) {
        console.error("Kon bericht uit query niet laden", e);
      }
    };

    loadData();
    openFromQuery();
  }, [messageId, currentUser, userLoading, router, loadData]);

  if (userLoading) {
    return (
      <div className="flex justify-center p-10">
        <p className="text-gray-500 animate-pulse">Loading user data...</p>
      </div>
    );
  }

  const handleDelete = async (id?: number) => {
    if (!id) return;
    if (!confirm("Ben je zeker dat je dit bericht wilt verwijderen?")) return;

    try {
      const res = await CommunicationService.remove(id);
      if (!res.ok) throw new Error(`Delete failed (${res.status})`);

      await loadData();

      if (selectedMessage?.id === id) {
        setModalOpen(false);
        setSelectedMessage(null);
      }
    } catch (err: any) {
      alert(err?.message || "Verwijderen mislukt.");
    }
  };

  const openModalForMessage = (msg: CommunicationMessageDTO) => {
    setSelectedMessage(msg);
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setSelectedMessage(null);

    if (router.query.messageId) {
      const { messageId, ...rest } = router.query;
      router.replace({ pathname: router.pathname, query: rest }, undefined, {
        shallow: true,
      });
    }
  };

  return (
    <>
      <Head>
        <title>Communicatie</title>
        <meta name="description" content="Exam app" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <Header />

      <div className="flex">
        <SideNav />

        <main
          className="md:mt-24 mx-auto md:w-3/5 lg:w-1/2 p-4 space-y-4"
          data-cy="communication-list-page"
        >
          <div className="flex items-center justify-between mb-4">
            <h1
              className="text-2xl font-bold"
              data-cy="communication-list-heading"
            >
              Communicatie
            </h1>
            <button
              onClick={() => router.push("/communicatie/new")}
              className="bg-green-600 text-white px-4 py-2 rounded"
              data-cy="communication-new-button"
            >
              Nieuw bericht
            </button>
          </div>

          {loading && (
            <p data-cy="communication-list-loading">Bezig met laden...</p>
          )}

          {error && (
            <div
              className="border border-red-400 bg-red-50 text-red-700 px-3 py-2 rounded"
              data-cy="communication-list-error"
            >
              {error}
            </div>
          )}

          {!loading && messages.length === 0 && !error && (
            <p data-cy="communication-list-empty">Geen berichten gevonden.</p>
          )}

          {!loading && messages.length > 0 && (
            <div className="overflow-x-auto">
              <table
                className="min-w-full border border-gray-200 rounded-xl shadow-md overflow-hidden text-sm"
                data-cy="communication-table"
              >
                <thead className="bg-client-primary text-white">
                  <tr>
                    <th className="py-2 px-3 text-left font-semibold text-sm">
                      ID
                    </th>
                    <th className="py-2 px-3 text-left font-semibold text-sm">
                      Titel
                    </th>
                    <th className="py-2 px-3 text-left font-semibold text-sm">
                      Categorie
                    </th>
                    <th className="py-2 px-3 text-left font-semibold text-sm">
                      Kanalen
                    </th>
                    <th className="py-2 px-3 text-left font-semibold text-sm">
                      Start
                    </th>
                    <th className="py-2 px-3 text-left font-semibold text-sm">
                      Status
                    </th>
                    <th className="py-2 px-3 text-left font-semibold text-sm">
                      Acties
                    </th>
                  </tr>
                </thead>

                <tbody>
                  {messages.map((msg, index) => (
                    <tr
                      key={msg.id}
                      className={`transition-colors cursor-pointer ${
                        index % 2 === 0 ? "bg-gray-50" : "bg-white"
                      } hover:bg-gray-100`}
                      onClick={() => openModalForMessage(msg)}
                      data-cy={`communication-row-${msg.id}`}
                    >
                      <td className="py-2 px-3">{msg.id}</td>
                      <td className="py-2 px-3">{msg.title}</td>
                      <td className="py-2 px-3">{msg.category}</td>
                      <td className="py-2 px-3">
                        {(msg.channels || []).join(", ")}
                      </td>
                      <td className="py-2 px-3">
                        {msg.startsAt
                          ? new Date(msg.startsAt).toLocaleString()
                          : "-"}
                      </td>
                      <td className="py-2 px-3">
                        {msg.active ? (
                          <span
                            className="inline-block px-2 py-0.5 rounded bg-green-100 text-green-700 text-xs"
                            data-cy={`communication-active-badge-${msg.id}`}
                          >
                            Actief
                          </span>
                        ) : (
                          <span
                            className="inline-block px-2 py-0.5 rounded bg-gray-100 text-gray-700 text-xs"
                            data-cy={`communication-inactive-badge-${msg.id}`}
                          >
                            Inactief
                          </span>
                        )}
                      </td>

                      <td className="py-2 px-3">
                        <div className="flex items-center gap-2">
                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              router.push(`/communicatie/${msg.id}`);
                            }}
                            className="inline-flex items-center justify-center px-4 py-1.5 rounded-full bg-blue-600 text-white text-xs font-medium shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-1"
                            data-cy={`communication-edit-button-${msg.id}`}
                          >
                            Bewerken
                          </button>

                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              handleDelete(msg.id);
                            }}
                            className="inline-flex items-center justify-center px-4 py-1.5 rounded-full bg-red-600 text-white text-xs font-medium shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-1"
                            data-cy={`communication-delete-button-${msg.id}`}
                          >
                            Verwijderen
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </main>
      </div>

      {modalOpen && selectedMessage && (
        <div
          className="fixed inset-0 z-40 flex items-center justify-center bg-black/40"
          data-cy="communication-detail-modal-overlay"
        >
          <div
            className="bg-white rounded-xl shadow-2xl max-w-xl w-full mx-4 p-6"
            data-cy="communication-detail-modal"
          >
            <h2
              className="text-xl font-semibold mb-2"
              data-cy="communication-detail-title"
            >
              {selectedMessage.title}
            </h2>
            <p
              className="text-sm text-gray-700 whitespace-pre-line mb-4"
              data-cy="communication-detail-body"
            >
              {selectedMessage.body}
            </p>

            <div className="grid grid-cols-2 gap-3 text-xs text-gray-600 mb-4">
              <div>
                <div className="font-semibold">Categorie</div>
                <div data-cy="communication-detail-category">
                  {selectedMessage.category}
                </div>
              </div>
              <div>
                <div className="font-semibold">Kanalen</div>
                <div data-cy="communication-detail-channels">
                  {(selectedMessage.channels || []).join(", ")}
                </div>
              </div>
              <div>
                <div className="font-semibold">Start</div>
                <div data-cy="communication-detail-start">
                  {selectedMessage.startsAt
                    ? new Date(selectedMessage.startsAt).toLocaleString()
                    : "-"}
                </div>
              </div>
              <div>
                <div className="font-semibold">Einde</div>
                <div data-cy="communication-detail-end">
                  {selectedMessage.endsAt
                    ? new Date(selectedMessage.endsAt).toLocaleString()
                    : "-"}
                </div>
              </div>
            </div>

            <div className="flex justify-end gap-2">
              <button
                className="px-4 py-2 rounded-full border border-gray-300 text-sm"
                onClick={closeModal}
                data-cy="communication-detail-close-button"
              >
                Sluiten
              </button>
              <button
                className="px-4 py-2 rounded-full bg-blue-600 text-white text-sm"
                onClick={() => {
                  closeModal();
                  if (selectedMessage.id) {
                    router.push(`/communicatie/${selectedMessage.id}`);
                  }
                }}
                data-cy="communication-detail-edit-button"
              >
                Bewerken
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default Communicatie;
