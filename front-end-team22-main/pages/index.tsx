// pages/index.tsx
import Head from "next/head";
import Image from "next/image";
import Header from "@components/header";
import SideNav from "@components/sideNav";
import { useUser } from "utils/UserContext";
import { useRouter } from "next/router";
import { useEffect } from "react";

const Home: React.FC = () => {
  const { currentUser, loading } = useUser();
  const router = useRouter();

  useEffect(() => {
    if (!loading && !currentUser) {
      router.push("/login");
    }
  }, [loading, currentUser, router]);

  if (loading || !currentUser) {
    return (
      <>
        <Head>
          <title>IPitup Portaal</title>
        </Head>
        <main className="min-h-screen flex items-center justify-center bg-client-bg text-white">
          <p className="text-sm opacity-80">Doorverwijzen naar login...</p>
        </main>
      </>
    );
  }

  return (
    <>
      <Head>
        <title>IPitup Portaal</title>
        <meta
          name="description"
          content="Beweeg mee met IPitup – samen actief in de publieke ruimte."
        />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Header />
      <div className="flex">
        <SideNav />
        <main className="text-center md:mt-24 mx-auto md:w-3/5 lg:w-1/2 px-6">
          <div className="space-y-6">
            <h1 className="text-4xl md:text-5xl font-bold text-teal-700">
              Beweeg mee met <span className="text-teal-500">IPitup</span>
            </h1>

            <p className="text-gray-700 text-lg md:text-xl leading-relaxed">
              IPitup brengt beweging naar de publieke ruimte. Met onze
              beweegbanken, tools en community maken we samen actief zijn
              eenvoudig, leuk en bereikbaar voor iedereen.
            </p>

            <p className="text-gray-600">
              Ontdek jouw stappenplan, volg je voortgang en draag bij aan een
              gezondere, actievere buurt.
            </p>
          </div>

          <div className="mt-16 flex justify-center">
            <Image
              src="/images/iPitup-bank.jpg"
              alt="iPitup beweegbank"
              width={600}
              height={400}
              className="rounded-2xl shadow-lg"
            />
          </div>
        </main>
      </div>
    </>
  );
};

export default Home;
