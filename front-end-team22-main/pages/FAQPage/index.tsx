import Head from "next/head";
import Header from "@components/header";
import SideNav from "@components/sideNav";
import FAQService from "@services/FAQService";
import { FAQ } from "@types";
import { useState, useEffect } from "react";
import { useRouter } from "next/router";
import { useUser } from "utils/UserContext";

const FAQPage: React.FC = () => {
  const router = useRouter();
  const { currentUser } = useUser();

  const [FAQs, setFAQs] = useState<FAQ[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchFAQ = async () => {
    try {
      setLoading(true);
      const data = await FAQService.getAll();
      setFAQs(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    // if (currentUser?.role?.toUpperCase() !== "ADMIN") {
    //   setError("Je hebt geen toestemming om deze actie uit te voeren.");
    //   return;
    // }
    try {
      setError(null);
      const res = await FAQService.remove(id);
      if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(text || `Verwijderen mislukt (${res.status})`);
      }
      // update lokale state zodat item verdwijnt
      setFAQs((prev) => prev.filter((faq) => faq.id !== id));
    } catch (err: any) {
      setError(err.message || "Er is een fout opgetreden bij verwijderen.");
    }
  };

  useEffect(() => {
    fetchFAQ();
  }, []);

  return (
    <>
      <Head>
        <title>FAQ</title>
        <meta name="description" content="Exam app" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Header />
      <div className="flex">
        {currentUser && <SideNav />}
        <main className="text-center md:mt-24 mx-auto md:w-3/5 lg:w-1/2">
          <div>
            <div className="text-3xl font-bold mb-6" data-cy="faq-title">
              Frequently Asked Questions
            </div>

            {loading ? (
              <p
                className="p-4 text-center text-gray-500"
                data-cy="FAQ-loading"
              >
                Loading...
              </p>
            ) : error ? (
              <p className="p-4 text-center text-red-500" data-cy="FAQ-error">
                Error: {error}
              </p>
            ) : (
              <div
                className="bg-white shadow-lg rounded-xl p-6 space-y-6 border border-gray-300"
                data-cy="FAQ-table-wrapper"
              >
                {FAQs.map((faq) => (
                  <div
                    key={faq.id}
                    className="p-6 bg-gray-50 rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow"
                    data-cy="FAQ-item"
                  >
                    <div className="flex items-start">
                      <div className="flex-1">
                        <h2
                          className="text-xl font-semibold text-gray-800 mb-2"
                          data-cy="FAQ-question"
                        >
                          {faq.question}
                        </h2>
                        <p className="text-gray-600" data-cy="FAQ-answer">
                          {faq.answer}
                        </p>
                      </div>
                      {currentUser?.role?.toUpperCase() === "ADMIN" && (
                        <button
                          onClick={() => handleDelete(faq.id!)}
                          className="ml-auto bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                          data-cy="FAQ-delete-button"
                        >
                          Verwijderen
                        </button>
                      )}
                    </div>
                  </div>
                ))}
                {currentUser?.role?.toUpperCase() === "ADMIN" && (
                  <button
                    onClick={() => router.push("/FAQPage/new")}
                    className="bg-darker-client-bg text-white px-4 py-2 rounded"
                    data-cy="communication-new-button"
                  >
                    Nieuwe vraag toevoegen
                  </button>
                )}
              </div>
            )}
          </div>
        </main>
      </div>
    </>
  );
};

export default FAQPage;
