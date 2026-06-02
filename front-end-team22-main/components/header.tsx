"use client";

import React, { useEffect, useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { Settings } from "lucide-react"; // Zorg dat lucide-react geïnstalleerd is

import CommunicationService from "@services/CommunicationService";
import InboxDropdown from "@components/users/InboxDropdown";
import { CommunicationMessageDTO } from "../types";
import { useUser } from "utils/UserContext";

const Header: React.FC = () => {
  const { currentUser } = useUser();
  const [platformModalMessage, setPlatformModalMessage] = useState<CommunicationMessageDTO | null>(null);
  const [platformModalOpen, setPlatformModalOpen] = useState(false);

  useEffect(() => {
    const run = async () => {
      if (!currentUser?.id) return;

      try {
        const res = await CommunicationService.getActiveForProfile(currentUser.id);
        if (!res.ok) return;

        const data: CommunicationMessageDTO[] = await res.json();
        const platformMessages = data.filter((m) =>
          m.channels?.includes("PLATFORM_POPUP" as any)
        );

        if (platformMessages.length > 0) {
          setPlatformModalMessage(platformMessages[0]);
          setPlatformModalOpen(true);
        }
      } catch (e) {
        console.error("Kon platform popups niet laden", e);
      }
    };

    run();
  }, [currentUser?.id]);

  return (
    <header className="bg-client-bg px-10 border-b border-gray-200">
      <div className="flex justify-between items-center py-3">
        {/* left side */}
        <div className="flex items-center space-x-6">
          <Link href="/">
            <Image
              src="/images/image.png"
              alt="Logo"
              width={250}
              height={250}
              priority
            />
          </Link>
          <nav className="flex items-center space-x-4"></nav>
        </div>

        {/* right side */}
        <div className="flex items-center space-x-4">
          <InboxDropdown variant="contact" />
          
          {currentUser ? (
            <>
              <InboxDropdown variant="profile" />
              <InboxDropdown variant="all" />
              <InboxDropdown variant="mobile" />

              <Link 
                href="/instellingen" 
                className="p-2 text-gray-600 hover:bg-gray-100 rounded-full transition-colors"
                title="Instellingen"
              >
                <Settings size={24} />
              </Link>

              <span className="text-black text-lg font-medium pl-2">
                Welcome: {currentUser.name ?? currentUser.email}
              </span>
            </>
          ) : (
            <Link
              href="/login"
              className="px-10 py-2 text-xl text-white hover:bg-gray-600 rounded-lg bg-darker-client-bg"
            >
              Login
            </Link>
          )}
        </div>
      </div>

      {/* PLATFORM_POPUP modal */}
      {platformModalOpen && platformModalMessage && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
          <div className="bg-white rounded-xl shadow-2xl max-w-lg w-full mx-4 p-6">
            <h2 className="text-xl font-semibold mb-2">
              {platformModalMessage.title}
            </h2>
            <p className="text-sm text-gray-700 whitespace-pre-line mb-4">
              {platformModalMessage.body}
            </p>
            <div className="flex justify-end">
              <button
                className="px-4 py-2 rounded-full bg-blue-600 text-white text-sm hover:bg-blue-700"
                onClick={() => setPlatformModalOpen(false)}
              >
                Sluiten
              </button>
            </div>
          </div>
        </div>
      )}
    </header>
  );
};

export default Header;