"use client";

import Head from "next/head";
import Image from "next/image";
import Link from "next/link";
import bcrypt from "bcryptjs";
import { useState } from "react";

const Login = () => {
  const [ErrorDiv, SetErrorMessage] = useState<JSX.Element | null>(null);
  const [showForm, setShowForm] = useState(false);

  const LoginAPICall = async () => {
    const form = document.getElementById("login-form") as HTMLFormElement;
    const userInput = form?.elements.namedItem("user") as HTMLInputElement;
    const passInput = form?.elements.namedItem("password") as HTMLInputElement;

    const UserName: string = userInput?.value ?? "";
    const Password: string = passInput?.value ?? "";

    if (UserName === "" || Password === "") {
      SetErrorMessage(
        <p className="text-red-700 text-sm p-3 mb-3 border border-red-400 rounded-full bg-red-100 text-center">
          Vul Alle Velden In
        </p>
      );
      return;
    }

    const userSalt = (await RequestSalt(UserName)).replaceAll('"', "");
    if (userSalt === "Unknown Profile!") {
      SetErrorMessage(
        <p className="text-red-700 text-sm p-3 mb-3 border border-red-400 rounded-full bg-red-100 text-center">
          Gebruiker niet bekend
        </p>
      );
      return;
    }

    DoAPICall(UserName, Password, userSalt).then((response) => {
      response.text().then((text) => {
        handleLoginResponse(text);
      });
    });
  };

  function handleLoginResponse(text: string) {
    text = text.replaceAll('"', "");
    if (text === "") {
      SetErrorMessage(
        <p className="text-red-700 text-sm p-3 mb-3 border border-red-400 rounded-full bg-red-100 text-center">
          An error occurred, please try again
        </p>
      );
    } else if (text === "Unknown Profile!") {
      SetErrorMessage(
        <p className="text-red-700 text-sm p-3 mb-3 border border-red-400 rounded-full bg-red-100 text-center">
          Gebruiker niet bekend
        </p>
      );
    } else if (text === "Wrong Password!") {
      SetErrorMessage(
        <p className="text-red-700 text-sm p-3 mb-3 border border-red-400 rounded-full bg-red-100 text-center">
          Incorrect Wachtwoord
        </p>
      );
    } else {
      document.cookie =
        "token=" +
        text +
        "; expires=" +
        new Date(Date.now() + 1000 * 60 * 60 * 24).toUTCString() +
        "; path=/";
      window.location.href = "/";
    }
  }

  async function RequestSalt(UserName: string): Promise<string> {
    const response: Response = await fetch(
      "/api/profile/salt?" + new URLSearchParams({ email: UserName }),
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.text();
  }

  async function DoAPICall(
    UserName: string,
    Password: string,
    userSalt: string
  ) {
    return await fetch("/api/profile/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: UserName,
        hashedPassword: bcrypt.hashSync(Password, userSalt.replace('"', "")),
      }),
    });
  }

  return (
    <>
      <Head>
        <title>Login</title>
      </Head>

      <main className="min-h-screen relative overflow-hidden flex items-center justify-center">
        <Image
          src="/images/iPitup-bank.jpg"
          alt="Background"
          fill
          priority
          className="object-cover"
        />

        {/* DARK OVERLAY */}
        <div className="absolute inset-0 bg-slate-900/40" />

        {/* LOGIN CARD */}
        <div className="relative z-10 w-full flex justify-center px-4">
          <div className="bg-client-primary max-w-xl w-full rounded-3xl shadow-2xl p-8 md:p-10">
            {/* Logo */}
            <div className="flex justify-between items-center mb-6">
              <Image
                src="/images/image.png"
                alt="IPitup logo"
                width={120}
                height={120}
              />
            </div>

            {!showForm ? (
              <>
                <h1 className="text-2xl md:text-3xl font-semibold text-black mb-4">
                  Welkom bij My IPitup!
                </h1>
                <p className="text-sm text-black mb-4">
                  Log in om toegang te krijgen tot je persoonlijke IPitup
                  omgeving:
                </p>
                <ul className="list-disc list-inside text-sm text-black mb-6 space-y-1">
                  <li>Informatie over jouw beweegbank(en)</li>
                  <li>Planning van activiteiten en sessies</li>
                  <li>Voorbereiding en materiaal op maat</li>
                  <li>Doelen en opvolging voor jouw doelgroep</li>
                </ul>

                <div className="flex flex-wrap gap-3 mb-6">
                  <Link
                    href="/contact"
                    className="px-4 py-2 rounded-full text-sm font-medium bg-black/10 hover:bg-black/15 text-black"
                  >
                    Contact
                  </Link>
                  <Link
                    href="/FAQPage"
                    className="px-4 py-2 rounded-full text-sm font-medium bg-black/10 hover:bg-black/15 text-black"
                  >
                    FAQ
                  </Link>
                </div>

                <button
                  type="button"
                  onClick={() => setShowForm(true)}
                  className="w-full mt-2 bg-black text-white py-3 rounded-full text-sm font-semibold hover:bg-zinc-900"
                >
                  Log in
                </button>
              </>
            ) : (
              <>
                <h1 className="text-2xl md:text-3xl font-semibold text-black mb-2">
                  Log in op My IPitup
                </h1>
                <p className="text-sm text-black/80 mb-4">
                  Vul je gegevens in om verder te gaan.
                </p>

                {ErrorDiv && (
                  <div data-cy="login-error" className="mb-3">
                    {ErrorDiv}
                  </div>
                )}

                <form
                  id="login-form"
                  onSubmit={(e) => {
                    e.preventDefault();
                    LoginAPICall();
                  }}
                  className="space-y-4"
                >
                  <div>
                    <label className="block text-sm font-medium text-black mb-1">
                      E-mail
                    </label>
                    <input
                      id="user"
                      name="user"
                      type="text"
                      className="w-full rounded-full px-4 py-3 text-sm border border-black/10"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-black mb-1">
                      Wachtwoord
                    </label>
                    <input
                      id="password"
                      name="password"
                      type="password"
                      className="w-full rounded-full px-4 py-3 text-sm border border-black/10"
                    />
                  </div>

                  <button
                    type="submit"
                    className="w-full bg-black text-white mt-2 py-3 rounded-full text-sm font-semibold hover:bg-zinc-900"
                  >
                    Login
                  </button>
                </form>

                <button
                  type="button"
                  onClick={() => setShowForm(false)}
                  className="mt-4 w-full text-xs text-black/80 hover:underline"
                >
                  &larr; Terug naar info
                </button>
              </>
            )}
          </div>
        </div>
      </main>
    </>
  );
};

export default Login;
