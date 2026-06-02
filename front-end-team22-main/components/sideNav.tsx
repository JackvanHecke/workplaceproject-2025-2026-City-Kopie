"use client";
import React from "react";
import Link from "next/link";
import { usePathname } from "next/navigation"; // 1. Importeer de hook
import { useUser } from "utils/UserContext";

const SideNav: React.FC = () => {
  const { currentUser, loading } = useUser();
  const pathname = usePathname();

  if (loading) {
    return (
      <div className="flex justify-center p-10">
        <p className="text-gray-500 animate-pulse">Loading user data...</p>
      </div>
    );
  }

  if (!currentUser) {
    return null;
  }

  const role = currentUser.role?.toUpperCase();
  const isAdmin = role === "ADMIN";
  const isClient = role === "CLIENT";
  const isCoach = role === "COACH";
  const isEndUser = role === "END_USER";

  const getNavLinkStyle = (href: string) => {
    const isActive = pathname === href || pathname.startsWith(`${href}/`);

    const baseStyle = "m-3 block px-10 py-4 text-xl rounded-lg w-64 box-border text-center transition-all";

    // We gebruiken blue-950 voor een zeer diepe blauwe kleur en text-white voor contrast
    const activeStyle = "bg-blue-950 text-white font-bold shadow-lg ring-1 ring-white/10";

    const inactiveStyle = "text-white bg-darker-client-bg hover:bg-gray-600 hover:text-lg";

    return `${baseStyle} ${isActive ? activeStyle : inactiveStyle}`;
  };

  return (
    <nav className="">
      <ul className="m-5 py-1 bg-client-bg">
        {(isAdmin || isClient || isCoach || isEndUser) && (
          <li>
            <Link href="/infrastructuur" className={getNavLinkStyle("/infrastructuur")}>
              Beweegtoestellen
            </Link>
          </li>
        )}

        {(isAdmin || isClient || isCoach || isEndUser) && (
          <li>
            <Link href="/activiteiten" className={getNavLinkStyle("/activiteiten")}>
              Activiteiten
            </Link>
          </li>
        )}

        {(isAdmin || isClient || isCoach) && (
          <li>
            <Link href="/communicatie" className={getNavLinkStyle("/communicatie")}>
              Communicatie
            </Link>
          </li>
        )}

        {(isAdmin || isClient) && (
          <li>
            <Link href="/monitoring" className={getNavLinkStyle("/monitoring")}>
              Monitoring
            </Link>
          </li>
        )}

        {(isAdmin || isClient) && (
          <li>
            <Link href="/onderhoud" className={getNavLinkStyle("/onderhoud")}>
              Onderhoud
            </Link>
          </li>
        )}

        {(isAdmin || isClient) && (
          <li>
            <Link href="/financien" className={getNavLinkStyle("/financien")}>
              Financiën
            </Link>
          </li>
        )}

        {(isAdmin || isClient) && (
          <li>
            <Link href="/partnerships" data-cy="nav-partnerships" className={getNavLinkStyle("/partnerships")}>
              Partnerships
            </Link>
          </li>
        )}

        {(isAdmin || isClient || isCoach) && (
          <li>
            <Link href="/marketing" className={getNavLinkStyle("/marketing")}>
              Marketing
            </Link>
          </li>
        )}
      </ul>
    </nav>
  );
};

export default SideNav;