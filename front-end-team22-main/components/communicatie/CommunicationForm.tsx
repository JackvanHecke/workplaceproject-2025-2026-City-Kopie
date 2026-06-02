// components/communicatie/CommunicationForm.tsx
import React, { useEffect, useState } from "react";
import {
  CommunicationCategory,
  CommunicationMessageCreateDTO,
  CommunicationMessageDTO,
  DeliveryChannel,
  ProfileRoleEnum,
  Profile,
} from "../../types";
import CommunicationService from "../../services/CommunicationService";
import CommunicationBasicsSection from "./CommunicationBasicsSection";
import CommunicationTimingSection from "./CommunicationTimingSection";
import CommunicationTargetingSection from "./CommunicationTargetingSection";
import CommunicationRecipientsSection from "./CommunicationRecipientsSection";
import UserService from "../../services/UserService";
import SectionCard from "./SectionCard";

type Props = {
  initialData?: CommunicationMessageDTO | null;
  onSubmit: (data: CommunicationMessageCreateDTO) => Promise<void> | void;
  submitLabel?: string;
};

const CommunicationForm: React.FC<Props> = ({
  initialData,
  onSubmit,
  submitLabel = "Opslaan",
}) => {
  const [title, setTitle] = useState("");
  const [body, setBody] = useState("");
  const [category, setCategory] = useState<CommunicationCategory>("OTHER");
  const [channels, setChannels] = useState<DeliveryChannel[]>(["APP_POPUP"]);
  const [startDate, setStartDate] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endDate, setEndDate] = useState("");
  const [endTime, setEndTime] = useState("");
  const [active, setActive] = useState(true);
  const [minAge, setMinAge] = useState<number | undefined>();
  const [maxAge, setMaxAge] = useState<number | undefined>();
  const [targetRoles, setTargetRoles] = useState<ProfileRoleEnum[]>([]);
  const [createdByProfileId, setCreatedByProfileId] = useState<number>(1);
  const [createdByName, setCreatedByName] = useState<string | undefined>();

  const [allProfiles, setAllProfiles] = useState<Profile[]>([]);
  const [profilesLoading, setProfilesLoading] = useState(false);
  const [profileSearch, setProfileSearch] = useState("");
  const [selectedRecipients, setSelectedRecipients] = useState<Profile[]>([]);

  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadCurrentUser = async () => {
      try {
        const user = await UserService.getCurrentUser();
        console.log("Current user in CommunicationForm:", user);

        if (user?.id) {
          setCreatedByProfileId(user.id);
        }
        if (user?.name) {
          setCreatedByName(user.name);
        } else if (user?.email) {
          setCreatedByName(user.email);
        }
      } catch (e) {
        console.error("Kon huidige gebruiker niet laden", e);
      }
    };

    // Always try to load current user (new or edit), but it will just be a fallback
    loadCurrentUser();
  }, []); // <-- no dependency on initialData anymore

  // Load profiles for search
  useEffect(() => {
    const loadProfiles = async () => {
      try {
        setProfilesLoading(true);
        const res = await CommunicationService.getRecipientProfiles();
        if (!res.ok) {
          const txt = await res.text().catch(() => "");
          console.error("Profielzoeklijst faalde:", res.status, txt);
          return;
        }
        const data = (await res.json()) as Profile[];
        setAllProfiles(data);
      } catch (e) {
        console.error("Kon profielen niet laden", e);
      } finally {
        setProfilesLoading(false);
      }
    };
    loadProfiles();
  }, []);

  // Prefill edit data
  useEffect(() => {
    if (!initialData) return;

    setTitle(initialData.title ?? "");
    setBody(initialData.body ?? "");
    setCategory(initialData.category ?? "OTHER");
    setChannels(initialData.channels ?? ["APP_POPUP"]);

    if (initialData.startsAt) {
      const d = new Date(initialData.startsAt);
      setStartDate(d.toISOString().slice(0, 10));
      setStartTime(d.toISOString().slice(11, 16));
    }

    if (initialData.endsAt) {
      const d = new Date(initialData.endsAt);
      setEndDate(d.toISOString().slice(0, 10));
      setEndTime(d.toISOString().slice(11, 16));
    }

    setActive(initialData.active ?? true);
    setMinAge(initialData.minAge ?? undefined);
    setTargetRoles(initialData.targetRoles ?? []);

    if (initialData.createdByProfileId) {
      setCreatedByProfileId(initialData.createdByProfileId);
    }
    if (initialData.createdByName) {
      setCreatedByName(initialData.createdByName);
    }

    if (
      initialData.explicitRecipients &&
      initialData.explicitRecipients.length > 0
    ) {
      const mapped: Profile[] = initialData.explicitRecipients.map((r) => ({
        id: r.id,
        name: r.name,
        email: undefined,
      }));
      setSelectedRecipients(mapped);
    }
  }, [initialData]);

  const toggleChannel = (channel: DeliveryChannel) => {
    setChannels((prev) =>
      prev.includes(channel)
        ? prev.filter((c) => c !== channel)
        : [...prev, channel]
    );
  };

  const toggleRole = (role: ProfileRoleEnum) => {
    setTargetRoles((prev) =>
      prev.includes(role) ? prev.filter((r) => r !== role) : [...prev, role]
    );
  };

  const addRecipient = (profile: Profile) => {
    setSelectedRecipients((prev) => [...prev, profile]);
    setProfileSearch("");
  };

  const removeRecipient = (profileId?: number) => {
    if (!profileId) return;
    setSelectedRecipients((prev) => prev.filter((p) => p.id !== profileId));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);

    try {
      const hasDateRange = !!startDate && !!startTime;
      const hasToggleActive = active === true;

      if (!hasDateRange && !hasToggleActive) {
        throw new Error(
          "Gebruik ofwel de 'Actief' schakelaar of vul een startdatum en -tijd in."
        );
      }

      let startsAtIso: string | undefined = undefined;
      let endsAtIso: string | undefined = undefined;

      if (hasDateRange) {
        startsAtIso = `${startDate}T${startTime}:00`;

        if (endDate) {
          const time = endTime || "00:00";
          endsAtIso = `${endDate}T${time}:00`;
        }
      }

      const explicitProfileIds = selectedRecipients
        .map((p) => p.id)
        .filter((id): id is number => typeof id === "number");

      const payload: CommunicationMessageCreateDTO = {
        title,
        body,
        category,
        channels,
        // if we have a date range, send it; otherwise let backend treat null as
        // “always active” when active === true
        startsAt: (startsAtIso ?? null) as any, // or adjust backend DTO to allow null
        endsAt: endsAtIso,
        active,
        minAge,
        maxAge,
        targetRoles,
        explicitProfileIds,
        createdByProfileId,
      };

      await onSubmit(payload);
    } catch (err: any) {
      console.error(err);
      setError(err?.message || "Er is een fout opgetreden.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      data-cy="communication-form"
      className="space-y-4 max-w-2xl mx-auto bg-white rounded-xl shadow-md p-6"
    >
      <h2 className="text-xl font-semibold mb-2" data-cy="communication-form-title">
        {initialData ? "Communicatie bewerken" : "Nieuw communicatiebericht"}
      </h2>

      {error && (
        <div
          className="border border-red-400 bg-red-50 text-red-700 px-3 py-2 rounded"
          data-cy="communication-error-alert"
        >
          {error}
        </div>
      )}

      <SectionCard title="Basisgegevens">
        <CommunicationBasicsSection
          title={title}
          body={body}
          category={category}
          channels={channels}
          active={active}
          onChangeTitle={setTitle}
          onChangeBody={setBody}
          onChangeCategory={setCategory}
          onToggleChannel={toggleChannel}
          onChangeActive={setActive}
        />
      </SectionCard>

      <SectionCard title="Tijdstip">
        <CommunicationTimingSection
          startDate={startDate}
          startTime={startTime}
          endDate={endDate}
          endTime={endTime}
          onChangeStartDate={setStartDate}
          onChangeStartTime={setStartTime}
          onChangeEndDate={setEndDate}
          onChangeEndTime={setEndTime}
        />
      </SectionCard>

      <SectionCard title="Doelgroep">
        <CommunicationTargetingSection
          minAge={minAge}
          maxAge={maxAge}
          targetRoles={targetRoles}
          onChangeMinAge={setMinAge}
          onChangeMaxAge={setMaxAge}
          onToggleRole={toggleRole}
        />
      </SectionCard>

      <SectionCard title="Ontvangers & creator">
        <CommunicationRecipientsSection
          allProfiles={allProfiles}
          profilesLoading={profilesLoading}
          profileSearch={profileSearch}
          selectedRecipients={selectedRecipients}
          createdByProfileId={createdByProfileId}
          createdByName={createdByName}
          onChangeProfileSearch={setProfileSearch}
          onAddRecipient={addRecipient}
          onRemoveRecipient={removeRecipient}
        />
      </SectionCard>

      <div className="pt-2 flex justify-end">
        <button
          type="submit"
          disabled={submitting}
          data-cy="communication-submit-button"
          className="inline-flex items-center justify-center px-5 py-2.5 rounded-full bg-blue-600 text-white text-sm font-medium shadow-sm hover:bg-blue-700 disabled:opacity-60 disabled:cursor-not-allowed transition"
        >
          {submitting ? "Bezig..." : submitLabel}
        </button>
      </div>
    </form>
  );
};

export default CommunicationForm;
