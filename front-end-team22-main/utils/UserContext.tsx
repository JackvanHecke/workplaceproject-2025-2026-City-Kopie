"use client";

import React, { createContext, useContext, useEffect, useState } from "react";
import UserService from "@services/UserService";

type CurrentUser = {
  id?: number;
  name?: string;
  email?: string;
  age?: number;
  gender?: string;
  nationality?: string;
  bmi?: number;
  movementMinutes?: number;
  registeredAt?: string;
  role?: string;
} | null;

interface UserContextValue {
  currentUser: CurrentUser;
  loading: boolean;
}

const UserContext = createContext<UserContextValue>({
  currentUser: null,
  loading: true,
});

export const UserProvider = ({ children }: { children: React.ReactNode }) => {
  const [currentUser, setCurrentUser] = useState<CurrentUser>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        const user = await UserService.getCurrentUser();
        setCurrentUser(user);
      } catch {
        setCurrentUser(null);
      } finally {
      setLoading(false);
      }
    };

    load();
  }, []);

  return (
    <UserContext.Provider value={{ currentUser, loading }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => useContext(UserContext);
