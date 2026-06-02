import React from "react";
import Head from "next/head";
import Header from "../../components/header";
import SideNav from "@components/sideNav";

import { getCounters, Counters } from "../../services/StatsService";
import BenchesSection from "../../components/infrastructuur/BenchesSection";
import Tellers from "../../components/infrastructuur/Tellers";
import BankVolgen from "../../components/infrastructuur/BankVolgen";
import Map from "../../components/infrastructuur/Map";
import { useFollowNotifications } from "../../hooks/useFollowNotification";

type Props = { counters: Counters | null; error?: string | null };

const DEMO_PROFILE_ID = 1;

const InfrastructuurPage: React.FC<Props> = ({ counters, error }) => {
  useFollowNotifications(DEMO_PROFILE_ID);

  return (
    <>
      <Head>
        <title>Beweegtoestellen</title>
      </Head>

      <Header />

      <div className="flex">
        <SideNav />
        <main className="p-4 md:w-4/5 mx-auto space-y-12">
          <BenchesSection />

          {error && (
            <div className="mb-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-red-700">
              {error}
            </div>
          )}

          
          <BankVolgen profileId={DEMO_PROFILE_ID} />
          <Map />
        </main>
      </div>
    </>
  );
};

export default InfrastructuurPage;

export async function getServerSideProps() {
  try {
    const counters = await getCounters();
    return { props: { counters } };
  } catch {
    return {
      props: {
        counters: null,
        error: "Kon counters niet laden. Controleer /api/counters of je dummy JSON.",
      },
    };
  }
}
