"use client";

import Head from "next/head";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

import Header from "@components/header";
import SideNav from "@components/sideNav";
import BenchForm from "@components/bench/BenchForm";
import MovementRouteTab from "@components/bench/MovementRouteTab";

import LocationService from "../../../services/LocationService";
import { Location } from "../../../types";

const EditBenchPage: React.FC = () => {
  const router = useRouter();
  const { id } = router.query;
  const [bench, setBench] = useState<Location | null>(null);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<"bench" | "route">("bench");

  useEffect(() => {
    if (!id) return;

    const load = async () => {
      try {
        const data = await LocationService.getById(id as string);
        setBench(data);
      } catch (err) {
        console.error("Failed to load bench", err);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [id]);

  const handleBenchSubmit = async (payload: any, photoFile?: File | null) => {
    if (!id) return;

    try {
      const updated = await LocationService.update(id as string, payload);
      let finalBench = updated;

      if (photoFile) {
        finalBench = await LocationService.uploadPhoto(id as string, photoFile);
      }

      setBench(finalBench);

      router.push("/infrastructuur");
    } catch (err) {
      console.error("Update failed", err);
      alert("Opslaan mislukt – zie console.");
    }
  };

  if (loading || !bench) {
    return (
      <>
        <Head>
          <title>Beweegtoestel laden...</title>
        </Head>
        <Header />
        <div className="flex bg-gray-50 min-h-[calc(100vh-80px)]">
          <SideNav />
          <main className="flex-1 p-4 md:p-8 flex items-center justify-center">
            <div className="bg-white shadow-md rounded-lg px-6 py-4 text-gray-600 text-sm">
              Beweegtoestel wordt geladen...
            </div>
          </main>
        </div>
      </>
    );
  }

  return (
    <>
      <Head>
        <title>
          Beweegtoestel bewerken · #{bench.id} {bench.benchName}
        </title>
      </Head>

      <Header />

      <div className="flex bg-gray-50 min-h-[calc(100vh-80px)]">
        <SideNav />

        <main className="flex-1 p-4 md:p-8 overflow-y-auto">
          <div className="max-w-5xl mx-auto bg-white shadow-lg rounded-xl p-4 md:p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h1 className="text-2xl font-bold text-gray-900">
                Beweegtoestel #{bench.id} · {bench.benchName}
              </h1>
              <button
                onClick={() => router.push("/infrastructuur")}
                className="text-xs md:text-sm text-blue-600 hover:underline"
              >
                &larr; Terug naar overzicht
              </button>
            </div>

            <div className="flex border-b mb-4">
              <button
                className={`px-4 py-2 font-medium text-sm md:text-base ${
                  activeTab === "bench"
                    ? "border-b-2 border-blue-600 text-blue-600"
                    : "text-gray-600 hover:text-gray-800"
                }`}
                onClick={() => setActiveTab("bench")}
              >
                Beweegtoestel
              </button>
              <button
                className={`px-4 py-2 font-medium text-sm md:text-base ${
                  activeTab === "route"
                    ? "border-b-2 border-blue-600 text-blue-600"
                    : "text-gray-600 hover:text-gray-800"
                }`}
                onClick={() => setActiveTab("route")}
              >
                Beweegroute
              </button>
            </div>

            {activeTab === "bench" ? (
              <BenchForm
                bench={bench}
                onSubmit={handleBenchSubmit}
                onCancel={() => router.push("/infrastructuur")}
              />
            ) : (
              <MovementRouteTab bench={bench} onBenchUpdated={setBench} />
            )}
          </div>
        </main>
      </div>
    </>
  );
};

export default EditBenchPage;
