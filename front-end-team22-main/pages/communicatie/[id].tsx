import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import Head from "next/head";

import Header from "@components/header";
import SideNav from "@components/sideNav";
import CommunicationForm from "../../components/communicatie/CommunicationForm";
import CommunicationService from "../../services/CommunicationService";
import {
  CommunicationMessageCreateDTO,
  CommunicationMessageDTO,
} from "../../types";

const EditCommunicationPage = () => {
  const router = useRouter();
  const { id } = router.query;

  const [initialData, setInitialData] =
    useState<CommunicationMessageDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    const fetchData = async () => {
      setLoading(true);
      setLoadError(null);
      try {
        const res = await CommunicationService.getById(id as string);
        if (!res.ok) {
          const txt = await res.text().catch(() => "");
          throw new Error(txt || `Kon bericht niet laden (${res.status})`);
        }
        const data = (await res.json()) as CommunicationMessageDTO;
        setInitialData(data);
      } catch (err: any) {
        console.error(err);
        setLoadError(err?.message || "Kon bericht niet laden.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const handleSubmit = async (payload: CommunicationMessageCreateDTO) => {
    if (!id) return;
    setSubmitError(null);
    const res = await CommunicationService.update(id as string, payload);
    if (!res.ok) {
      const txt = await res.text().catch(() => "");
      throw new Error(txt || `Bijwerken mislukt (${res.status})`);
    }
    router.push("/communicatie");
  };

  return (
    <>
      <Head>
        <title>Communicatie bewerken</title>
      </Head>

      <Header />

      <div className="flex">
        <SideNav />

        <main
  className="md:mt-24 mx-auto md:w-3/5 lg:w-1/2 p-4 space-y-4 max-h-[80vh] overflow-y-auto"          data-cy="communication-edit-page"
        >
          <h1
            className="text-2xl font-bold"
            data-cy="communication-edit-heading"
          >
            Communicatie bewerken
          </h1>

          {loading && (
            <p data-cy="communication-edit-loading">Bezig met laden...</p>
          )}

          {loadError && (
            <div
              className="border border-red-400 bg-red-50 text-red-700 px-3 py-2 rounded"
              data-cy="communication-edit-load-error"
            >
              {loadError}
            </div>
          )}

          {!loading && initialData && (
            <>
              {submitError && (
                <div
                  className="border border-red-400 bg-red-50 text-red-700 px-3 py-2 rounded"
                  data-cy="communication-edit-submit-error"
                >
                  {submitError}
                </div>
              )}

              <CommunicationForm
                initialData={initialData}
                onSubmit={async (data) => {
                  try {
                    await handleSubmit(data);
                  } catch (e: any) {
                    setSubmitError(
                      e?.message || "Er is een fout opgetreden."
                    );
                  }
                }}
                submitLabel="Opslaan"
              />
            </>
          )}
        </main>
      </div>
    </>
  );
};

export default EditCommunicationPage;
