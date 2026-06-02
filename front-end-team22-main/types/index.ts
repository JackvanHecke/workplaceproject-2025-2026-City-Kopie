export type Checklists = {
  id?: number;
  name?: string;
  info?: string;
  completionRecords?: CompletedChecklistItem[];
};

export type CompletedChecklistItem = {
  id: {
    benchId: number;
    checklistId: number;
  };
  completedAt: string;
};

export type Phase = {
  id?: number;
  name?: string;
  checklists?: Checklists[];
};

export type Note = {
  id: number;
  profile: {
    id: number;
    name: string;
    email: string;
  };
  content: string;
  createdAt: string;
  updatedAt: string;
};

export type GroupedStep = {
  id: number;
  title: string;
  info: string;
  icon: string;
  items: Checklists[];
};

// types/index.ts

export type User = {
  id?: number;
  name?: string;
  age?: number;
  email?: string;
  password?: string;
  role?: string;
};

export type StatusMessage = {
  message: string;
  type: "error" | "success";
};

export type Stakeholder = {
  id?: number;
  organisation: string;
  contactPerson: string;
  email: string;
  phoneNumber: string;
  role: string;
  partnershipDecided?: boolean;
};

export type Step = {
  title: string;
  items: string[];
  icon: string;
};

export type Role = "coach" | "client" | "all";

export type RadiusOptionKm = 1 | 5 | 10 | number;

export type Profile = {
  id?: number;
  name?: string;
  age?: number;
  email?: string;
  gender?: string;
  nationality?: string;
  bmi?: number;
  performedExercises?: number;
  performedTrainingSessions?: number;
  numberOfBenches?: number;
  movementMinutes?: number;
  registeredAt?: string;
};

// types/index.ts

export type MovementRoute = {
  id?: number;
  name?: string;
  type?: string; // e.g. "walking", "running", "cycling", ...
  // later you can add gpxUrl/geoJson/etc
};

export type Location = {
  id?: number;
  benchName?: string;
  owner?: string;
  street?: string;
  houseNumber?: string;
  zipCode?: string;
  city?: string;
  country?: string;
  size?: string;
  type?: string; // "bench" | "wall"
  connectedRoutes?: number;
  tags?: string[];

  showInApp?: boolean;
  mobile?: boolean;
  publicAvailable?: boolean;

  latitude?: number;
  longitude?: number;
  stations?: string[];

  photoUrl?: string;
  movementHistory?: any[];
  completedChecklistItems?: CompletedChecklistItem[];

  // movement route info
  movementRouteId?: number;
  movementRouteName?: string;
  movementRouteType?: string;
};

export type CoachOfferDTO = {
  id?: number;
  offerType?: string;
  targetGroup?: string;

  description?: string;
  freeOrPaid?: "free" | "paid" | string;
  price?: number;

  recurrence?: string;
  startDatetime?: string;
  endDatetime?: string;
  createdAt?: string;
};

export type CoachAvailabilityDTO = {
  id?: number;
  availableFrom: string;
  availableTo: string;
  note?: string;
  isRecurring?: boolean;
  recurrenceRule?: string;
  bench?: Location;
  createdAt?: string;
};

export type Price = {
  amount: number;
  currency?: string;
};

export type FAQ = {
  id?: number;
  question: String;
  answer: string;
};

export type Coach = {
  id?: number;

  bio?: string;
  name?: string;
  isAvailable?: boolean;
  notifyNewDevices?: boolean;
  defaultRadiusKm?: number;
  createdAt?: string;

  offers?: CoachOfferDTO[];
  availability?: CoachAvailabilityDTO[];
  locations?: Location[];

  role?: Role;
  subscribeToNewEquipment?: boolean;
  subscriptionRadiusKm?: RadiusOptionKm;

  isFree?: boolean;
  price?: Price;

  updatedAt?: string;
};

export type CommunicationRecipientDTO = {
  id?: number;
  name?: string;
  role?: ProfileRoleEnum;
};

export type CommunicationMessageDTO = {
  id?: number;

  // WHAT
  title?: string;
  body?: string;
  category?: CommunicationCategory;

  // HOW
  channels?: DeliveryChannel[];

  // WHEN
  startsAt?: string;
  endsAt?: string;
  active?: boolean;

  // WHO (filters)
  minAge?: number;
  maxAge?: number;
  targetRoles?: ProfileRoleEnum[];

  // META
  createdByProfileId?: number;
  createdByName?: string;
  createdAt?: string;

  // Explicit recipients
  explicitRecipients?: CommunicationRecipientDTO[];
};

export type CommunicationMessageCreateDTO = {
  // WHAT
  title: string;
  body: string;
  category: CommunicationCategory;

  // HOW
  channels: DeliveryChannel[];

  // WHEN
  startsAt: string; // ISO
  endsAt?: string;
  active: boolean;

  // WHO (filters)
  minAge?: number;
  maxAge?: number;
  targetRoles?: ProfileRoleEnum[];

  // WHO (explicit profiles)
  explicitProfileIds?: number[];

  // For now: pass creator explicitly
  createdByProfileId: number;
};

export type Device = {
  id?: number;
  region: string;
};

// ENUMS

export type CommunicationCategory =
  | "INAUGURATION"
  | "ACTIVITY"
  | "COURSE_SERIES"
  | "CHALLENGE"
  | "OTHER";

export type DeliveryChannel =
  | "APP_POPUP"
  | "PROFILE_MESSAGE"
  | "DEVICE_POPUP"
  | "PLATFORM_POPUP"
  | "MOBILE_POPUP";

export type ProfileRoleEnum = "END_USER" | "COACH" | "CLIENT" | "ADMIN";

export type ProfileInboxItemDTO = {
  id: number;
  messageId: number;
  title: string;
  preview: string;
  read: boolean;
  deliveredAt: string;
  channels?: DeliveryChannel[];
};

export type Workshop = {
  id?: number;
  naam: string;
  zichtbaarInApp: boolean;
  startDatum: string;
  eindDatum: string;
  tijdstip: string;
  doelgroep: string;
  prijs: number;
  betalenVia: string;
  organisator: string;
  contactPersoon: string;
  inschrijvenVerplicht: boolean;
  inschrijvenVia: string;
  communicatieViaApp: boolean;
  location: Location;
};

export type PartnershipCategory = {
  id: number;
  code: string;
  label: string;
  sortIndex: number;
};

export type LocationPartnershipDTO = {
  categoryId: number;
  decided: boolean;
};
