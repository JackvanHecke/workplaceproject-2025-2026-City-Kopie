"use client";

import React, { useEffect, useState } from "react";
import Head from "next/head";
import Image from "next/image";

import Header from "@components/header";
import SideNav from "@components/sideNav";
import { useUser } from "utils/UserContext";

const ProfilePage: React.FC = () => {
  const { currentUser } = useUser();
  const [profileImage, setProfileImage] = useState(
    "/images/default-profile.jpg"
  );

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("nl-BE", {
      day: "2-digit",
      month: "long",
      year: "numeric",
    });
  };

  const checkFileExists = async (path: string): Promise<string> => {
    try {
      const res = await fetch(path, { method: "HEAD" });
      if (res.ok) {
        return path; // file exists
      }
      return "/images/default-profile.jpg"; // fallback
    } catch {
      return "/images/default-profile.jpg"; // fallback
    }
  };
  const handleLogout = () => {
    if (typeof document !== "undefined") {
      document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
    }
    window.location.href = "/login";
  };

  useEffect(() => {
    if (currentUser?.email) {
      const namePart = currentUser.email.split(".")[0].toLowerCase();
      const path = `/images/${namePart}.jpeg`;
      checkFileExists(path).then(setProfileImage);
    }
  }, [currentUser]);

  return (
    <>
      <Head>
        <title>Profiel</title>
        <meta name="description" content="Exam app" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Header />
      <div className="flex flex-col md:flex-row min-h-screen bg-gradient-to-b via-gray-800 to-gray-900">
        <SideNav />
        <main className="flex-1 flex flex-col items-center justify-start px-4 pt-0">
          {/* Title & Subtitle */}
          <div className="p-8 text-center mb-12">
            <h1 className="text-5xl md:text-6xl font-extrabold text-darker-client-bg mb-4">
              Profiel
            </h1>
            <p className="text-lg md:text-xl text-darker-client-bg">
              Bekijk je persoonlijke gegevens
            </p>
          </div>

          {currentUser ? (
            <div className="rounded-3xl p-8 md:p-12 max-w-4xl w-full flex flex-col md:flex-row gap-8 shadow-2xl transform scale-105 items-center">
              {/* Profile Image */}
              <div className="flex-shrink-0">
                <Image
                  src={profileImage}
                  alt="Profile Picture"
                  width={150}
                  height={150}
                  className="rounded-full object-cover"
                />
              </div>

              {/* User Info */}
              <div className="flex-1 grid grid-cols-1 md:grid-cols-2 gap-6">
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>Naam:</strong> {currentUser.name}
                </p>
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>Leeftijd:</strong> {currentUser.age}
                </p>
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>Email:</strong> {currentUser.email}
                </p>
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>Geslacht:</strong> {currentUser.gender}
                </p>
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>Nationaliteit:</strong> {currentUser.nationality}
                </p>
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>BMI:</strong> {currentUser.bmi}
                </p>
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>Bewegingsminuten:</strong>{" "}
                  {currentUser.movementMinutes}
                </p>
                <p className="text-2xl font-medium text-darker-client-bg">
                  <strong>Geregistreerd op:</strong>{" "}
                  {formatDate(currentUser.registeredAt)}
                </p>
                <button
                  onClick={handleLogout}
                  className="px-6 py-2 text-xl text-white hover:bg-gray-600 rounded-lg bg-darker-client-bg"
                >
                  Logout
                </button>
              </div>
            </div>
          ) : (
            <p className="text-3xl text-darker-client-bg mt-8">
              Geen gebruiker ingelogd.
            </p>
          )}
        </main>
      </div>
    </>
  );
};

export default ProfilePage;
