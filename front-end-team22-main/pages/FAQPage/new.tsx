// pages/FAQPage/new.tsx
import { useRouter } from "next/router";
import Head from "next/head";
import { useState } from "react";

import Header from "@components/header";
import SideNav from "@components/sideNav";

import { FAQ } from "../../types";
import FAQForm from "@components/FAQ/FAQForm";
import FAQService from "@services/FAQService";

const NewFAQPage = () => {
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (data: FAQ) => {
    setError(null);
    try {
      const res = await FAQService.create(data);
      if (!res.ok) {
        const text = await res.text().catch(() => "");
        throw new Error(text || `Aanmaken mislukt (${res.status})`);
      }
      router.push("/FAQPage");
    } catch (e: any) {
      setError(e?.message || "Er is een fout opgetreden.");
    }
  };

  return (
    <>
      <Head>
        <title>Nieuwe FAQ toevoegen</title>
      </Head>

      <Header />

      <div className="flex">
        <SideNav />

        <main
          className="md:mt-24 mx-auto md:w-3/5 lg:w-1/2 p-4 space-y-4 max-h-[80vh] overflow-y-auto"
          data-cy="FAQ-new-page"
        >
          {error && (
            <div
              className="border border-red-400 bg-red-50 text-red-700 px-3 py-2 rounded"
              data-cy="FAQ-new-error"
            >
              {error}
            </div>
          )}

          <FAQForm onSubmit={handleSubmit} submitLabel="Aanmaken" />
        </main>
      </div>
    </>
  );
};

export default NewFAQPage;
