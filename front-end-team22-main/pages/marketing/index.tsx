// pages/marketing/index.tsx
"use client";

import Head from "next/head";
import React, { useEffect, useState } from "react";
import Header from "@components/header";
import SideNav from "@components/sideNav";
import { useRouter } from "next/navigation";
import { useUser } from "utils/UserContext";

type UploadedAsset = {
  id: string;
  file: File;
};

const MarketingPage: React.FC = () => {
  const [assets, setAssets] = useState<UploadedAsset[]>([]);
  const router = useRouter();
  const { currentUser, loading } = useUser();

  useEffect(() => {
    if (!loading && !currentUser) {
      router.push('/login');
    }
  }, [currentUser, loading, router]);

  if (loading) {
    return (
      <div className="flex justify-center p-10">
        <p className="text-gray-500 animate-pulse">Loading user data...</p>
      </div>
    );
  }

  const isAdmin = currentUser.role?.toUpperCase() === 'ADMIN';
  const isClient = currentUser.role?.toUpperCase() === 'CLIENT';

  const handleFileChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const files = Array.from(e.target.files ?? []);
    if (!files.length) return;

    setAssets((prev) => [
      ...prev,
      ...files.map((file) => ({
        id: `${file.name}-${file.lastModified}-${Math.random()}`,
        file,
      })),
    ]);
  };

  const handleDownloadUploaded = (asset: UploadedAsset) => {
    const url = URL.createObjectURL(asset.file);
    const a = document.createElement("a");
    a.href = url;
    a.download = asset.file.name;
    a.click();
    URL.revokeObjectURL(url);
  };

  const flyerTemplates = [
    {
      title: "Vip Score Formulier",
      desc: "Flyer rond beweegbanken in de buurt.",
      accent: "bg-teal-400",
      downloadPath:
        "/marketing-assets/1702566789_TheVIPCupscoreformulier.pdf",
    },
    {
      title: "Draaiboek",
      desc: "Communicatie naar scholen en jeugdverenigingen.",
      accent: "bg-orange-400",
      downloadPath:
        "/marketing-assets/1708077575_DraaiboekJeugdolympiadeVIP2024IPitup.pdf",
    },
    {
      title: "Overzicht VIP aanbod",
      desc: "Promotie voor IPitup als partner in vitaliteit.",
      accent: "bg-purple-400",
      downloadPath:
        "/marketing-assets/1708086139_OverzichtVIP2024aanbodIPitup.pdf",
    },
  ];

  return (
    <>
      <Head>
        <title>Marketing</title>
        <meta
          name="description"
          content="Marketingmateriaal en promotiemiddelen"
        />
      </Head>

      <Header />

      <div className="flex bg-gray-50 min-h-[calc(100vh-80px)]">
        <SideNav />

        <main className="flex-1 p-4 md:p-8 overflow-y-auto">
          <div className="max-w-6xl mx-auto space-y-8">

            {isAdmin || isClient ? (
              <section className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
                <div className="flex flex-col md:flex-row md:items-start md:justify-between gap-4">
                  <div>
                    <h1 className="text-2xl font-bold text-gray-900 mb-2">
                      Marketingmateriaal beheren
                    </h1>
                    <p className="text-sm text-gray-600 mb-4 max-w-xl">
                      Upload hier je eigen flyers, video&apos;s, teksten en andere
                      promotiematerialen. Deze zijn alleen zichtbaar voor jouw
                      organisatie (voorlopig enkel in deze sessie).
                    </p>
                    <ul className="list-disc list-inside text-sm text-gray-600 space-y-1">
                      <li>Upload eigen flyers, video&apos;s, PDF&apos;s en afbeeldingen</li>
                      <li>Download je geüploade bestanden lokaal</li>
                      <li>Combineer ze met IPitup-templates hieronder</li>
                    </ul>
                  </div>
                  <div className="w-full md:w-80">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                      Upload jouw materiaal
                    </label>
                    <div className="border-2 border-dashed border-teal-400 rounded-xl p-4 bg-teal-50/40 text-center">
                      <input
                        type="file"
                        multiple
                        onChange={handleFileChange}
                        className="block w-full text-sm text-gray-600 mb-2"
                      />
                      <p className="text-xs text-gray-500">
                        Ondersteund: afbeeldingen, PDF, video, tekstbestanden.
                        (Nog niet permanent opgeslagen.)
                      </p>
                    </div>

                    {assets.length > 0 && (
                      <div className="mt-4 max-h-40 overflow-y-auto pr-1">
                        <h3 className="text-xs font-semibold text-gray-700 mb-1">
                          Jouw geüploade bestanden (lokaal)
                        </h3>
                        <ul className="space-y-1 text-xs">
                          {assets.map((asset) => (
                            <li
                              key={asset.id}
                              className="flex items-center justify-between bg-gray-50 border border-gray-200 rounded px-2 py-1"
                            >
                              <span className="truncate mr-2">
                                {asset.file.name}
                              </span>
                              <button
                                type="button"
                                onClick={() =>
                                  handleDownloadUploaded(asset)
                                }
                                className="text-teal-700 hover:text-teal-900 font-medium"
                              >
                                Download
                              </button>
                            </li>
                          ))}
                        </ul>
                      </div>
                    )}
                  </div>
                </div>
              </section>
            ): null}

            <section className="space-y-4">
              <div className="flex items-center justify-between">
                <h2 className="text-xl font-semibold text-gray-900">
                  IPitup-voorbeeldflyers
                </h2>
                <p className="text-xs text-gray-500">
                  Officiële templates die je vrij mag gebruiken en aanpassen.
                </p>
              </div>

              <div className="grid gap-4 md:grid-cols-3">
                {flyerTemplates.map((flyer) => (
                  <article
                    key={flyer.title}
                    className="bg-white rounded-xl shadow-md border border-gray-100 overflow-hidden flex flex-col"
                  >
                    <div
                      className={`${flyer.accent} h-24 relative flex items-center justify-center`}
                    >
                      <span className="uppercase tracking-wide text-xs text-white/90">
                        IPitup-template
                      </span>
                    </div>
                    <div className="p-4 flex-1 flex flex-col">
                      <h3 className="text-sm font-semibold text-gray-900 mb-1">
                        {flyer.title}
                      </h3>
                      <p className="text-xs text-gray-600 mb-3 flex-1">
                        {flyer.desc}
                      </p>
                      <a
                        href={flyer.downloadPath}
                        download
                        className="mt-auto inline-flex items-center text-xs text-teal-700 font-medium hover:text-teal-900"
                      >
                        Download voorbeeld (PDF)
                      </a>
                    </div>
                  </article>
                ))}
              </div>
            </section>

            <section className="space-y-4">
              <h2 className="text-xl font-semibold text-gray-900">
                Gepersonaliseerde video
              </h2>
              <p className="text-sm text-gray-600 max-w-2xl">
                In elk van deze blokken kan je later een gepersonaliseerde
                video tonen (bijvoorbeeld per gemeente, doelgroep of campagne).
                Voor nu tonen we een placeholder waar je later een embed
                (YouTube, Vimeo, eigen hosting) kan plaatsen.
              </p>

              <div className="grid gap-4 md:grid-cols-3">
                {[
                  "Introductie IPitup",
                ].map((label) => (
                  <article
                    key={label}
                    className="bg-white rounded-xl shadow-md border border-gray-100 p-4 flex flex-col"
                  >
                    <h3 className="text-sm font-semibold text-gray-900 mb-2">
                      {label}
                    </h3>
                    <div className="aspect-video rounded-lg bg-black flex items-center justify-center text-xs text-gray-300 mb-3">
                      Video placeholder – hier kan een
                      <br />
                      gepersonaliseerde video komen.
                    </div>
                    <p className="text-xs text-gray-600 flex-1">
                      Gebruik dit blok om een specifieke doelgroep aan te
                      spreken met een korte video.
                    </p>
                  </article>
                ))}
              </div>
            </section>
          </div>
        </main>
      </div>
    </>
  );
};

export default MarketingPage;
