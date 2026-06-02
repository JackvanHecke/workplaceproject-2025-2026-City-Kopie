import { useRouter } from "next/router";
import Head from "next/head";
import { useState } from "react";

import Header from "@components/header";
import SideNav from "@components/sideNav";
import CommunicationForm from "../../components/communicatie/CommunicationForm";
import CommunicationService from "../../services/CommunicationService";
import { CommunicationMessageCreateDTO } from "../../types";

const NewCommunicationPage = () => {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (data: CommunicationMessageCreateDTO) => {
    setError(null);
    const res = await CommunicationService.create(data);
    if (!res.ok) {
      const text = await res.text().catch(() => "");
      throw new Error(text || `Aanmaken mislukt (${res.status})`);
    }
    router.push("/communicatie");
  };

  return (
    <>
      <Head>
        <title>Nieuw communicatiebericht</title>
      </Head>

      <Header />

      <div className="flex">
        <SideNav />

       <main
  className="md:mt-24 mx-auto md:w-3/5 lg:w-1/2 p-4 space-y-4 max-h-[80vh] overflow-y-auto"
  data-cy="communication-new-page"
>

          {error && (
            <div
              className="border border-red-400 bg-red-50 text-red-700 px-3 py-2 rounded"
              data-cy="communication-new-error"
            >
              {error}
            </div>
          )}

          <CommunicationForm
            onSubmit={async (data) => {
              try {
                await handleSubmit(data);
              } catch (e: any) {
                setError(e?.message || "Er is een fout opgetreden.");
              }
            }}
            submitLabel="Aanmaken"
          />
        </main>
      </div>
    </>
  );
};

export default NewCommunicationPage;
