import Head from "next/head";
// import Image from "next/image";
import Header from "@components/header";
// import styles from "@styles/home.module.css";
import SideNav from "@components/sideNav";

const Financien: React.FC = () => {
  return (
    <>
      <Head>
        <title>Financiën</title>
        <meta name="description" content="Exam app" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Header></Header>
      <div className="flex">
        <SideNav></SideNav>
        <main className="text-center md:mt-24 mx-auto md:w-3/5 lg:w-1/2">
          <div>
            <p>Financiën</p>
          </div>
        </main>
      </div>
    </>
  );
};

export default Financien;
