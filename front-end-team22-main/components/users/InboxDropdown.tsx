import React, { useEffect, useState, useRef } from "react";
import { useRouter } from "next/router";
import { ProfileInboxItemDTO, DeliveryChannel } from "../../types";
import UserService from "../../services/UserService";

type InboxVariant = "all" | "mobile" | "profile" | "contact";

type Props = {
  variant?: InboxVariant;
};

const InboxDropdown: React.FC<Props> = ({ variant = "all" }) => {
  const router = useRouter();
  const [items, setItems] = useState<ProfileInboxItemDTO[]>([]);
  const [open, setOpen] = useState(false);
  const [profileId, setProfileId] = useState<number | null>(null);

  const containerRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    let alive = true;
    const load = async () => {
      const user = await UserService.getCurrentUser();
      if (!alive || !user?.id) return;

      setProfileId(user.id);

      const res = await fetch(`/api/profile/${user.id}/inbox?limit=4`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });

      if (!alive || !res.ok) return;

      const data: ProfileInboxItemDTO[] = await res.json();
      setItems(data);
    };

    load();
    const t = setInterval(load, 30_000); //elke 30 seconden

    return () => {
      alive = false;
      clearInterval(t);
    };
  }, []);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const filteredItems =
    variant === "mobile"
      ? items.filter((i) =>
          i.channels?.includes("MOBILE_POPUP" as DeliveryChannel)
        )
      : items;

  const unreadCount = filteredItems.filter((i) => !i.read).length;

  const iconClass =
    "relative flex items-center justify-center w-10 h-10 rounded-full border border-gray-300 bg-white hover:bg-gray-100";

  const handleClickItem = async (item: ProfileInboxItemDTO) => {
    setOpen(false);

    // 1) Markeer als gelezen (backend + frontend state)
    if (profileId && item.id) {
      try {
        await fetch(`/api/profile/${profileId}/inbox/${item.id}/read`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
        });

        // direct badge updaten (zonder 30s te wachten)
        setItems((prev) =>
          prev.map((x) => (x.id === item.id ? { ...x, read: true } : x))
        );
      } catch (e) {
        console.error("Kon inbox item niet als gelezen markeren", e);
      }
    }

    if (item.messageId) {
      router.push(`/communicatie?messageId=${item.messageId}`);
    } else {
      router.push("/communicatie");
    }
  };

  const handleProfileClick = () => {
    router.push("/profiel");
  };

  const handleContactClick = () => {
    router.push("/contact");
  };

  return (
    <div className="relative" ref={containerRef}>
      <button
        type="button"
        className={iconClass}
        onClick={
          variant === "profile"
            ? handleProfileClick
            : variant === "contact"
            ? handleContactClick
            : () => setOpen((o) => !o)
        }
        aria-label={
          variant === "mobile"
            ? "Mobiele notificaties"
            : variant === "profile"
            ? "Profiel"
            : variant === "contact"
            ? "Contact"
            : "Inbox"
        }
      >
        {variant === "mobile" ? (
          // Phone icon
          <svg
            className="w-5 h-5 text-gray-700"
            fill="none"
            stroke="currentColor"
            strokeWidth={1.8}
            viewBox="0 0 24 24"
          >
            <rect x="7" y="3" width="10" height="18" rx="2" />
            <line x1="10" y1="6" x2="14" y2="6" />
            <circle cx="12" cy="18" r="1" />
          </svg>
        ) : variant === "profile" ? (
          // Profile icon
          <svg
            className="w-5 h-5 text-gray-700"
            fill="none"
            stroke="currentColor"
            strokeWidth={1.8}
            viewBox="0 0 24 24"
          >
            <circle cx="12" cy="8" r="4" />
            <path d="M4 20c0-4 4-6 8-6s8 2 8 6" />
          </svg>
        ) : variant === "contact" ? (
          // Question mark icon
          <svg
            className="w-5 h-5 text-gray-700"
            fill="none"
            stroke="currentColor"
            strokeWidth={1.8}
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M7.5 8.5a4.5 4.5 0 1 1 7.4 3.3c-1.1 1-2 1.9-2 4.3"
            />

            <circle cx="13" cy="21" r="0.5" />
          </svg>
        ) : (
          // Bell icon
          <svg
            className="w-5 h-5 text-gray-700"
            fill="none"
            stroke="currentColor"
            strokeWidth={1.8}
            viewBox="0 0 24 24"
          >
            <path d="M12 22a2 2 0 0 0 2-2H10a2 2 0 0 0 2 2Z" />
            <path d="M18 16v-5a6 6 0 1 0-12 0v5l-1.5 2H19.5L18 16Z" />
          </svg>
        )}

        {variant !== "profile" && variant !== "contact" && unreadCount > 0 && (
          <span className="absolute -top-1 -right-1 min-w-[18px] px-1.5 h-[18px] rounded-full bg-red-600 text-white text-xs flex items-center justify-center">
            {unreadCount}
          </span>
        )}
      </button>

      {variant !== "profile" && variant !== "contact" && open && (
        <div className="absolute right-0 mt-2 w-80 bg-white border border-gray-200 rounded-lg shadow-lg z-30">
          <div className="px-3 py-2 border-b border-gray-100 flex justify-between items-center">
            <span className="text-sm font-semibold">
              {variant === "mobile" ? "Mobiele notificaties" : "Berichten"}
            </span>
          </div>

          {filteredItems.length === 0 ? (
            <div className="px-3 py-3 text-sm text-gray-500">
              {variant === "mobile"
                ? "Geen mobiele notificaties."
                : "Geen berichten."}
            </div>
          ) : (
            <ul className="max-h-80 overflow-y-auto divide-y divide-gray-100">
              {filteredItems.map((item) => (
                <li
                  key={item.id}
                  className="px-3 py-2 text-sm cursor-pointer hover:bg-gray-50"
                  onClick={() => handleClickItem(item)}
                >
                  <div className="font-medium">{item.title}</div>
                  <div className="text-xs text-gray-500">{item.preview}</div>

                  {variant === "mobile" && (
                    <p className="mt-1 text-xs text-gray-400">
                      Je zou deze notificatie ook in de mobiele app moeten zien
                      (niet geïmplementeerd in deze versie).
                    </p>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
};

export default InboxDropdown;
