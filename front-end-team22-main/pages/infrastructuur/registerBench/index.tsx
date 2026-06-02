// pages/infrastructuur/registerBench/index.tsx
"use client";

import Head from "next/head";
import React, { useState } from "react";
import { useRouter } from "next/router";

import Header from "@components/header";
import SideNav from "@components/sideNav";
import BenchForm from "@components/bench/BenchForm";

import LocationService from "../../../services/LocationService";
import { Location } from "../../../types";

const RegisterBenchPage: React.FC = () => {
  const router = useRouter();
  const [saving, setSaving] = useState(false);

  const emptyBench: Location = {};

  const handleCreateBench = async (payload: any, photoFile?: File | null) => {
    try {
      setSaving(true);

      const created = await LocationService.create(payload);
      let finalBench = created;

      if (photoFile && created.id != null) {
        finalBench = await LocationService.uploadPhoto(created.id, photoFile);
      }

      router.push("/infrastructuur");
    } catch (err) {
      console.error("Create bench failed", err);
      alert("Aanmaken van beweegtoestel is mislukt – zie console.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <>
      <Head>
        <title>Nieuw beweegtoestel registreren</title>
        <meta name="description" content="Beweegbank registreren" />
      </Head>

      <Header />

      <div className="flex bg-gray-50 min-h-[calc(100vh-80px)]">
        <SideNav />

        <main className="flex-1 p-4 md:p-8 overflow-y-auto">
          <div className="max-w-5xl mx-auto bg-white shadow-lg rounded-xl p-4 md:p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h1 className="text-2xl font-bold text-gray-900">
                Nieuw beweegtoestel registreren
              </h1>
              {saving && (
                <span className="text-xs md:text-sm text-gray-500 animate-pulse">
                  Opslaan...
                </span>
              )}
            </div>

            <p className="text-sm text-gray-600 mb-6">
              Vul alle gegevens in voor deze beweegbank. Je kunt deze later
              altijd nog aanpassen.
            </p>

            <BenchForm
              bench={emptyBench}
              onSubmit={handleCreateBench}
              onCancel={() => router.push("/infrastructuur")}
            />
          </div>
        </main>
      </div>
    </>
  );
};

export default RegisterBenchPage;
