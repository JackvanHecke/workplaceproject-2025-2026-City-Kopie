import Head from "next/head";
import Header from "@components/header";
import SideNav from "@components/sideNav";
import { useUser } from "utils/UserContext";
const Contact: React.FC = () => {
  const { currentUser } = useUser();

  return (
    <>
      <Head>
        <title>Contact</title>
        <meta name="description" content="Exam app" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Header></Header>
      <div className="flex">
        {currentUser && <SideNav></SideNav>}
        {/* <main className="flex-1 w-full">
          <div className="h-full w-full bg-client-bg my-5 p-10 flex justify-center items-start">
            <div className="flex w-full"> */}
        <main className="flex-1 w-full overflow-auto">
          <div className="mx-auto mt-8 p-8 max-w-7xl">
            <div className="grid md:grid-cols-2 gap-10">
              <div className="bg-client-bg rounded-3xl p-8 shadow-md backdrop-blur-sm">
                <form className="space-y-6">
                  <div>
                    <label className="block font-semibold mb-1">
                      Voornaam *
                    </label>
                    <input required className="w-full p-2 rounded-xl" />
                  </div>

                  <div>
                    <label className="block font-semibold mb-1">Naam *</label>
                    <input required className="w-full p-2 rounded-xl" />
                  </div>

                  <div>
                    <label className="block font-semibold mb-1">
                      Organisatie *
                    </label>
                    <input required className="w-full p-2 rounded-xl" />
                  </div>

                  <div>
                    <label className="block font-semibold mb-1">
                      Telefoonnummer *
                    </label>
                    <input required className="w-full p-2 rounded-xl" />
                  </div>

                  <div>
                    <label className="block font-semibold mb-1">
                      E-mailadres *
                    </label>
                    <input required className="w-full p-2 rounded-xl" />
                  </div>

                  <div>
                    <label className="block font-semibold mb-1">
                      Uw bericht: *
                    </label>
                    <textarea
                      required
                      className="w-full p-2 rounded-xl"
                    ></textarea>
                  </div>

                  <div className="flex items-center space-x-2">
                    <input required type="checkbox" />
                    <span>Ik ga akkoord met de privacy policy. *</span>
                  </div>

                  <button
                    type="submit"
                    className="w-full hover:bg-gray-600 bg-darker-client-bg transition text-white py-3 rounded-full font-semibold"
                    // onClick={handleSubmit}
                  >
                    Verstuur
                  </button>
                  {/* <a
                    href="mailto:info@ipitup.eu"
                    className="w-full bg-teal-600 text-white py-3 rounded-full font-semibold hover:bg-teal-700 transition text-center block"
                  >
                    Verstuur
                  </a> */}
                </form>
              </div>

              <div className="mx-5 space-y-6">
                <h1 className="text-black text-5xl">Contacteer IPitup</h1>

                <div className="text-lg space-y-1">
                  <p className="text-gray-500">
                    Varentstraat 33 <br />
                    B-3118 Werchter
                  </p>
                  <a
                    className="underline"
                    href="mailto:info@ipitup.eu"
                    target="_blank"
                  >
                    info@ipitup.eu
                  </a>
                  <br />
                  <br />
                  <a
                    className="underline"
                    href="tel:003216436847"
                    target="_blank"
                  >
                    T +32 (0)16 43 68 47
                  </a>
                  <br />
                  <br />
                  <a
                    className="underline"
                    href="tel:0032472340508"
                    target="_blank"
                  >
                    M +32 (0)472 34 05 08
                  </a>
                </div>

                <div className="w-full h-80 overflow-hidden">
                  <iframe
                    src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2512.4926892158305!2d4.7163129887356074!3d50.97520129872041!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x47c15e999008b30b%3A0xbaee6bea0f94d4f6!2sVarentstraat%2033%2C%203118%20Rotselaar!5e0!3m2!1snl!2sbe!4v1726054287047!5m2!1snl!2sbe"
                    className="w-full h-full border-0"
                    loading="lazy"
                    allowFullScreen
                  ></iframe>
                </div>
                <h1 className="text-black text-3xl">Vragen?</h1>
                <div>
                  <a href="/FAQPage" className="underline text-lg">
                    Bekijk de veelgestelde vragen pagina
                  </a>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </>
  );
};

export default Contact;
