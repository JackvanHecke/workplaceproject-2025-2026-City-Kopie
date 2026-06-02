package be.ucll.model;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "LOCATIONS")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bench_id")
    private Long benchId;

    @Column(name = "bench_name")
    private String benchName;

    @Column(name = "bench_owner")
    private String benchOwner;

    @Column(name = "bench_city")
    private String benchCity;

    @Column(name = "bench_country")
    private String benchCountry;

    @OneToMany(mappedBy = "location")
    @JsonBackReference
    private List<Workshop> workshops = new ArrayList<>();

    @ManyToMany(mappedBy = "locations")
    @JsonBackReference
    private List<Stakeholder> stakeholders = new ArrayList<>();

    @OneToMany(mappedBy = "location")
    @JsonManagedReference("location-completion")
    private Set<CompletedChecklistItem> completedChecklistItems = new HashSet<>();

    @Column(name = "bench_size")
    private String benchSize;

    @Column(name = "bench_type")
    private String benchType;

    @Column(name = "connected_routes")
    private int connectedRoutes;

    @Column(name = "bench_street")
    private String benchStreet;

    @Column(name = "bench_house_number")
    private String benchHouseNumber;

    @Column(name = "bench_postal_code")
    private String benchPostalCode;

    @Column(name = "show_in_app")
    private Boolean showInApp;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "stations") // e.g. "push-up bench,pull-up bench"
    private String stations;

    // Top 10 exercises
    private Long top10_1_exercise_id;
    private Long top10_2_exercise_id;
    private Long top10_3_exercise_id;
    private Long top10_4_exercise_id;
    private Long top10_5_exercise_id;
    private Long top10_6_exercise_id;
    private Long top10_7_exercise_id;
    private Long top10_8_exercise_id;
    private Long top10_9_exercise_id;
    private Long top10_10_exercise_id;

    // Exercises per day
    private int exercises_per_day_monday;
    private int exercises_per_day_tuesday;
    private int exercises_per_day_wednesday;
    private int exercises_per_day_thursday;
    private int exercises_per_day_friday;
    private int exercises_per_day_saturday;
    private int exercises_per_day_sunday;

    // Exercises per hour
    private int exercise_per_hour_00;
    private int exercise_per_hour_01;
    private int exercise_per_hour_02;
    private int exercise_per_hour_03;
    private int exercise_per_hour_04;
    private int exercise_per_hour_05;
    private int exercise_per_hour_06;
    private int exercise_per_hour_07;
    private int exercise_per_hour_08;
    private int exercise_per_hour_09;
    private int exercise_per_hour_10;
    private int exercise_per_hour_11;
    private int exercise_per_hour_12;
    private int exercise_per_hour_13;
    private int exercise_per_hour_14;
    private int exercise_per_hour_15;
    private int exercise_per_hour_16;
    private int exercise_per_hour_17;
    private int exercise_per_hour_18;
    private int exercise_per_hour_19;
    private int exercise_per_hour_20;
    private int exercise_per_hour_21;
    private int exercise_per_hour_22;
    private int exercise_per_hour_23;

    // Exercises per gender
    private int exercises_gender_m;
    private int exercises_gender_f;

    // Exercises per age group
    private int exercises_age_0_17;
    private int exercises_age_18_24;
    private int exercises_age_25_34;
    private int exercises_age_35_44;
    private int exercises_age_45_54;
    private int exercises_age_55_64;
    private int exercises_age_65_plus;

    // Exercises per BMI group
    private int exercises_bmi_0_18_5;
    private int exercises_bmi_18_5_25;
    private int exercises_bmi_25_30;
    private int exercises_bmi_30_35;
    private int exercises_bmi_35_plus;

    private int movement_minutes;

    @Column(name = "gender_group")
    private String gender_group;

    private String tags;

    @Column(name = "is_mobile")
    private Boolean mobile;

    @Column(name = "is_public")
    private Boolean publicAvailable;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "movement_route_id")
    private MovementRoute movementRoute;
    private Instant createdAt;

    public Location() {}

    public Location(
            Long benchId,
            String benchName,
            String benchOwner,
            String benchCity,
            String benchCountry,
            String benchSize,
            String benchType,
            int connectedRoutes,
            Long top1, Long top2, Long top3, Long top4, Long top5, Long top6, Long top7, Long top8, Long top9,
            Long top10,
            int mon, int tue, int wed, int thu, int fri, int sat, int sun,
            int h00, int h01, int h02, int h03, int h04, int h05, int h06, int h07, int h08, int h09,
            int h10, int h11, int h12, int h13, int h14, int h15, int h16, int h17, int h18, int h19,
            int h20, int h21, int h22, int h23,
            int genderM, int genderF,
            int age0_17, int age18_24, int age25_34, int age35_44, int age45_54, int age55_64, int age65plus,
            int bmi0_18_5, int bmi18_5_25, int bmi25_30, int bmi30_35, int bmi35plus,
            int movementMinutes,
            String gender_group,
            String tags) {
        this.benchId = benchId;
        this.benchName = benchName;
        this.benchOwner = benchOwner;
        this.benchCity = benchCity;
        this.benchCountry = benchCountry;
        this.benchSize = benchSize;
        this.benchType = benchType;
        this.connectedRoutes = connectedRoutes;
        this.top10_1_exercise_id = top1;
        this.top10_2_exercise_id = top2;
        this.top10_3_exercise_id = top3;
        this.top10_4_exercise_id = top4;
        this.top10_5_exercise_id = top5;
        this.top10_6_exercise_id = top6;
        this.top10_7_exercise_id = top7;
        this.top10_8_exercise_id = top8;
        this.top10_9_exercise_id = top9;
        this.top10_10_exercise_id = top10;
        this.exercises_per_day_monday = mon;
        this.exercises_per_day_tuesday = tue;
        this.exercises_per_day_wednesday = wed;
        this.exercises_per_day_thursday = thu;
        this.exercises_per_day_friday = fri;
        this.exercises_per_day_saturday = sat;
        this.exercises_per_day_sunday = sun;
        this.exercise_per_hour_00 = h00;
        this.exercise_per_hour_01 = h01;
        this.exercise_per_hour_02 = h02;
        this.exercise_per_hour_03 = h03;
        this.exercise_per_hour_04 = h04;
        this.exercise_per_hour_05 = h05;
        this.exercise_per_hour_06 = h06;
        this.exercise_per_hour_07 = h07;
        this.exercise_per_hour_08 = h08;
        this.exercise_per_hour_09 = h09;
        this.exercise_per_hour_10 = h10;
        this.exercise_per_hour_11 = h11;
        this.exercise_per_hour_12 = h12;
        this.exercise_per_hour_13 = h13;
        this.exercise_per_hour_14 = h14;
        this.exercise_per_hour_15 = h15;
        this.exercise_per_hour_16 = h16;
        this.exercise_per_hour_17 = h17;
        this.exercise_per_hour_18 = h18;
        this.exercise_per_hour_19 = h19;
        this.exercise_per_hour_20 = h20;
        this.exercise_per_hour_21 = h21;
        this.exercise_per_hour_22 = h22;
        this.exercise_per_hour_23 = h23;
        this.exercises_gender_m = genderM;
        this.exercises_gender_f = genderF;
        this.exercises_age_0_17 = age0_17;
        this.exercises_age_18_24 = age18_24;
        this.exercises_age_25_34 = age25_34;
        this.exercises_age_35_44 = age35_44;
        this.exercises_age_45_54 = age45_54;
        this.exercises_age_55_64 = age55_64;
        this.exercises_age_65_plus = age65plus;
        this.exercises_bmi_0_18_5 = bmi0_18_5;
        this.exercises_bmi_18_5_25 = bmi18_5_25;
        this.exercises_bmi_25_30 = bmi25_30;
        this.exercises_bmi_30_35 = bmi30_35;
        this.exercises_bmi_35_plus = bmi35plus;
        this.movement_minutes = movementMinutes;
        this.gender_group = gender_group;
        this.tags = tags;
    }

    public List<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<Workshop> workshops) {
        this.workshops = workshops;
    }

    public List<Stakeholder> getStakeholders() {
        return stakeholders;
    }

    public void setStakeholders(List<Stakeholder> stakeholders) {
        this.stakeholders = stakeholders;
    }

    public Set<CompletedChecklistItem> getCompletedChecklistItems() {
        return completedChecklistItems;
    }

    public void setCompletedChecklistItems(Set<CompletedChecklistItem> completedChecklistItems) {
        this.completedChecklistItems = completedChecklistItems;
    }

    public Long getBenchId() {
        return benchId;
    }

    public void setBenchId(Long benchId) {
        this.benchId = benchId;
    }

    public String getBenchName() {
        return benchName;
    }

    public void setBenchName(String benchName) {
        this.benchName = benchName;
    }

    public String getBenchOwner() {
        return benchOwner;
    }

    public void setBenchOwner(String benchOwner) {
        this.benchOwner = benchOwner;
    }

    public String getBenchCity() {
        return benchCity;
    }

    public void setBenchCity(String benchCity) {
        this.benchCity = benchCity;
    }

    public String getBenchCountry() {
        return benchCountry;
    }

    public void setBenchCountry(String benchCountry) {
        this.benchCountry = benchCountry;
    }

    public String getBenchSize() {
        return benchSize;
    }

    public void setBenchSize(String benchSize) {
        this.benchSize = benchSize;
    }

    public String getBenchType() {
        return benchType;
    }

    public void setBenchType(String benchType) {
        this.benchType = benchType;
    }

    public int getConnectedRoutes() {
        return connectedRoutes;
    }

    public void setConnectedRoutes(int connectedRoutes) {
        this.connectedRoutes = connectedRoutes;
    }

    public Long getTop10_1_exercise_id() {
        return top10_1_exercise_id;
    }

    public void setTop10_1_exercise_id(Long top10_1_exercise_id) {
        this.top10_1_exercise_id = top10_1_exercise_id;
    }

    public Long getTop10_2_exercise_id() {
        return top10_2_exercise_id;
    }

    public void setTop10_2_exercise_id(Long top10_2_exercise_id) {
        this.top10_2_exercise_id = top10_2_exercise_id;
    }

    public Long getTop10_3_exercise_id() {
        return top10_3_exercise_id;
    }

    public void setTop10_3_exercise_id(Long top10_3_exercise_id) {
        this.top10_3_exercise_id = top10_3_exercise_id;
    }

    public Long getTop10_4_exercise_id() {
        return top10_4_exercise_id;
    }

    public void setTop10_4_exercise_id(Long top10_4_exercise_id) {
        this.top10_4_exercise_id = top10_4_exercise_id;
    }

    public Long getTop10_5_exercise_id() {
        return top10_5_exercise_id;
    }

    public void setTop10_5_exercise_id(Long top10_5_exercise_id) {
        this.top10_5_exercise_id = top10_5_exercise_id;
    }

    public Long getTop10_6_exercise_id() {
        return top10_6_exercise_id;
    }

    public void setTop10_6_exercise_id(Long top10_6_exercise_id) {
        this.top10_6_exercise_id = top10_6_exercise_id;
    }

    public Long getTop10_7_exercise_id() {
        return top10_7_exercise_id;
    }

    public void setTop10_7_exercise_id(Long top10_7_exercise_id) {
        this.top10_7_exercise_id = top10_7_exercise_id;
    }

    public Long getTop10_8_exercise_id() {
        return top10_8_exercise_id;
    }

    public void setTop10_8_exercise_id(Long top10_8_exercise_id) {
        this.top10_8_exercise_id = top10_8_exercise_id;
    }

    public Long getTop10_9_exercise_id() {
        return top10_9_exercise_id;
    }

    public void setTop10_9_exercise_id(Long top10_9_exercise_id) {
        this.top10_9_exercise_id = top10_9_exercise_id;
    }

    public Long getTop10_10_exercise_id() {
        return top10_10_exercise_id;
    }

    public void setTop10_10_exercise_id(Long top10_10_exercise_id) {
        this.top10_10_exercise_id = top10_10_exercise_id;
    }

    public int getExercises_per_day_monday() {
        return exercises_per_day_monday;
    }

    public void setExercises_per_day_monday(int exercises_per_day_monday) {
        this.exercises_per_day_monday = exercises_per_day_monday;
    }

    public int getExercises_per_day_tuesday() {
        return exercises_per_day_tuesday;
    }

    public void setExercises_per_day_tuesday(int exercises_per_day_tuesday) {
        this.exercises_per_day_tuesday = exercises_per_day_tuesday;
    }

    public int getExercises_per_day_wednesday() {
        return exercises_per_day_wednesday;
    }

    public void setExercises_per_day_wednesday(int exercises_per_day_wednesday) {
        this.exercises_per_day_wednesday = exercises_per_day_wednesday;
    }

    public int getExercises_per_day_thursday() {
        return exercises_per_day_thursday;
    }

    public void setExercises_per_day_thursday(int exercises_per_day_thursday) {
        this.exercises_per_day_thursday = exercises_per_day_thursday;
    }

    public int getExercises_per_day_friday() {
        return exercises_per_day_friday;
    }

    public void setExercises_per_day_friday(int exercises_per_day_friday) {
        this.exercises_per_day_friday = exercises_per_day_friday;
    }

    public int getExercises_per_day_saturday() {
        return exercises_per_day_saturday;
    }

    public void setExercises_per_day_saturday(int exercises_per_day_saturday) {
        this.exercises_per_day_saturday = exercises_per_day_saturday;
    }

    public int getExercises_per_day_sunday() {
        return exercises_per_day_sunday;
    }

    public void setExercises_per_day_sunday(int exercises_per_day_sunday) {
        this.exercises_per_day_sunday = exercises_per_day_sunday;
    }

    public int getExercise_per_hour_00() {
        return exercise_per_hour_00;
    }

    public void setExercise_per_hour_00(int exercise_per_hour_00) {
        this.exercise_per_hour_00 = exercise_per_hour_00;
    }

    public int getExercise_per_hour_01() {
        return exercise_per_hour_01;
    }

    public void setExercise_per_hour_01(int exercise_per_hour_01) {
        this.exercise_per_hour_01 = exercise_per_hour_01;
    }

    public int getExercise_per_hour_02() {
        return exercise_per_hour_02;
    }

    public void setExercise_per_hour_02(int exercise_per_hour_02) {
        this.exercise_per_hour_02 = exercise_per_hour_02;
    }

    public int getExercise_per_hour_03() {
        return exercise_per_hour_03;
    }

    public void setExercise_per_hour_03(int exercise_per_hour_03) {
        this.exercise_per_hour_03 = exercise_per_hour_03;
    }

    public int getExercise_per_hour_04() {
        return exercise_per_hour_04;
    }

    public void setExercise_per_hour_04(int exercise_per_hour_04) {
        this.exercise_per_hour_04 = exercise_per_hour_04;
    }

    public int getExercise_per_hour_05() {
        return exercise_per_hour_05;
    }

    public void setExercise_per_hour_05(int exercise_per_hour_05) {
        this.exercise_per_hour_05 = exercise_per_hour_05;
    }

    public int getExercise_per_hour_06() {
        return exercise_per_hour_06;
    }

    public void setExercise_per_hour_06(int exercise_per_hour_06) {
        this.exercise_per_hour_06 = exercise_per_hour_06;
    }

    public int getExercise_per_hour_07() {
        return exercise_per_hour_07;
    }

    public void setExercise_per_hour_07(int exercise_per_hour_07) {
        this.exercise_per_hour_07 = exercise_per_hour_07;
    }

    public int getExercise_per_hour_08() {
        return exercise_per_hour_08;
    }

    public void setExercise_per_hour_08(int exercise_per_hour_08) {
        this.exercise_per_hour_08 = exercise_per_hour_08;
    }

    public int getExercise_per_hour_09() {
        return exercise_per_hour_09;
    }

    public void setExercise_per_hour_09(int exercise_per_hour_09) {
        this.exercise_per_hour_09 = exercise_per_hour_09;
    }

    public int getExercise_per_hour_10() {
        return exercise_per_hour_10;
    }

    public void setExercise_per_hour_10(int exercise_per_hour_10) {
        this.exercise_per_hour_10 = exercise_per_hour_10;
    }

    public int getExercise_per_hour_11() {
        return exercise_per_hour_11;
    }

    public void setExercise_per_hour_11(int exercise_per_hour_11) {
        this.exercise_per_hour_11 = exercise_per_hour_11;
    }

    public int getExercise_per_hour_12() {
        return exercise_per_hour_12;
    }

    public void setExercise_per_hour_12(int exercise_per_hour_12) {
        this.exercise_per_hour_12 = exercise_per_hour_12;
    }

    public int getExercise_per_hour_13() {
        return exercise_per_hour_13;
    }

    public void setExercise_per_hour_13(int exercise_per_hour_13) {
        this.exercise_per_hour_13 = exercise_per_hour_13;
    }

    public int getExercise_per_hour_14() {
        return exercise_per_hour_14;
    }

    public void setExercise_per_hour_14(int exercise_per_hour_14) {
        this.exercise_per_hour_14 = exercise_per_hour_14;
    }

    public int getExercise_per_hour_15() {
        return exercise_per_hour_15;
    }

    public void setExercise_per_hour_15(int exercise_per_hour_15) {
        this.exercise_per_hour_15 = exercise_per_hour_15;
    }

    public int getExercise_per_hour_16() {
        return exercise_per_hour_16;
    }

    public void setExercise_per_hour_16(int exercise_per_hour_16) {
        this.exercise_per_hour_16 = exercise_per_hour_16;
    }

    public int getExercise_per_hour_17() {
        return exercise_per_hour_17;
    }

    public void setExercise_per_hour_17(int exercise_per_hour_17) {
        this.exercise_per_hour_17 = exercise_per_hour_17;
    }

    public int getExercise_per_hour_18() {
        return exercise_per_hour_18;
    }

    public void setExercise_per_hour_18(int exercise_per_hour_18) {
        this.exercise_per_hour_18 = exercise_per_hour_18;
    }

    public int getExercise_per_hour_19() {
        return exercise_per_hour_19;
    }

    public void setExercise_per_hour_19(int exercise_per_hour_19) {
        this.exercise_per_hour_19 = exercise_per_hour_19;
    }

    public int getExercise_per_hour_20() {
        return exercise_per_hour_20;
    }

    public void setExercise_per_hour_20(int exercise_per_hour_20) {
        this.exercise_per_hour_20 = exercise_per_hour_20;
    }

    public int getExercise_per_hour_21() {
        return exercise_per_hour_21;
    }

    public void setExercise_per_hour_21(int exercise_per_hour_21) {
        this.exercise_per_hour_21 = exercise_per_hour_21;
    }

    public int getExercise_per_hour_22() {
        return exercise_per_hour_22;
    }

    public void setExercise_per_hour_22(int exercise_per_hour_22) {
        this.exercise_per_hour_22 = exercise_per_hour_22;
    }

    public int getExercise_per_hour_23() {
        return exercise_per_hour_23;
    }

    public void setExercise_per_hour_23(int exercise_per_hour_23) {
        this.exercise_per_hour_23 = exercise_per_hour_23;
    }

    public int getExercises_gender_m() {
        return exercises_gender_m;
    }

    public void setExercises_gender_m(int exercises_gender_m) {
        this.exercises_gender_m = exercises_gender_m;
    }

    public int getExercises_gender_f() {
        return exercises_gender_f;
    }

    public void setExercises_gender_f(int exercises_gender_f) {
        this.exercises_gender_f = exercises_gender_f;
    }

    public int getExercises_age_0_17() {
        return exercises_age_0_17;
    }

    public void setExercises_age_0_17(int exercises_age_0_17) {
        this.exercises_age_0_17 = exercises_age_0_17;
    }

    public int getExercises_age_18_24() {
        return exercises_age_18_24;
    }

    public void setExercises_age_18_24(int exercises_age_18_24) {
        this.exercises_age_18_24 = exercises_age_18_24;
    }

    public int getExercises_age_25_34() {
        return exercises_age_25_34;
    }

    public void setExercises_age_25_34(int exercises_age_25_34) {
        this.exercises_age_25_34 = exercises_age_25_34;
    }

    public int getExercises_age_35_44() {
        return exercises_age_35_44;
    }

    public void setExercises_age_35_44(int exercises_age_35_44) {
        this.exercises_age_35_44 = exercises_age_35_44;
    }

    public int getExercises_age_45_54() {
        return exercises_age_45_54;
    }

    public void setExercises_age_45_54(int exercises_age_45_54) {
        this.exercises_age_45_54 = exercises_age_45_54;
    }

    public int getExercises_age_55_64() {
        return exercises_age_55_64;
    }

    public void setExercises_age_55_64(int exercises_age_55_64) {
        this.exercises_age_55_64 = exercises_age_55_64;
    }

    public int getExercises_age_65_plus() {
        return exercises_age_65_plus;
    }

    public void setExercises_age_65_plus(int exercises_age_65_plus) {
        this.exercises_age_65_plus = exercises_age_65_plus;
    }

    public int getExercises_bmi_0_18_5() {
        return exercises_bmi_0_18_5;
    }

    public void setExercises_bmi_0_18_5(int exercises_bmi_0_18_5) {
        this.exercises_bmi_0_18_5 = exercises_bmi_0_18_5;
    }

    public int getExercises_bmi_18_5_25() {
        return exercises_bmi_18_5_25;
    }

    public void setExercises_bmi_18_5_25(int exercises_bmi_18_5_25) {
        this.exercises_bmi_18_5_25 = exercises_bmi_18_5_25;
    }

    public int getExercises_bmi_25_30() {
        return exercises_bmi_25_30;
    }

    public void setExercises_bmi_25_30(int exercises_bmi_25_30) {
        this.exercises_bmi_25_30 = exercises_bmi_25_30;
    }

    public int getExercises_bmi_30_35() {
        return exercises_bmi_30_35;
    }

    public void setExercises_bmi_30_35(int exercises_bmi_30_35) {
        this.exercises_bmi_30_35 = exercises_bmi_30_35;
    }

    public int getExercises_bmi_35_plus() {
        return exercises_bmi_35_plus;
    }

    public void setExercises_bmi_35_plus(int exercises_bmi_35_plus) {
        this.exercises_bmi_35_plus = exercises_bmi_35_plus;
    }

    public int getMovement_minutes() {
        return movement_minutes;
    }

    public void setMovement_minutes(int movement_minutes) {
        this.movement_minutes = movement_minutes;
    }

    public String getGender_group() {
        return gender_group;
    }
    public void setGender_group(String gender_group) {
        this.gender_group = gender_group;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void addWorkshop(Workshop workshop) {
        workshops.add(workshop);
        workshop.setLocation(this);
    }

    public void addWorkshops(List<Workshop> workshopsInput) {
        for (Workshop workshop: workshopsInput) {
            addWorkshop(workshop);
        }
    }

    //Helper method
    public int getExercise_per_hour(int hour) {
        return switch (hour) {
            case 0 -> getExercise_per_hour_00();
            case 1 -> getExercise_per_hour_01();
            case 2 -> getExercise_per_hour_02();
            case 3 -> getExercise_per_hour_03();
            case 4 -> getExercise_per_hour_04();
            case 5 -> getExercise_per_hour_05();
            case 6 -> getExercise_per_hour_06();
            case 7 -> getExercise_per_hour_07();
            case 8 -> getExercise_per_hour_08();
            case 9 -> getExercise_per_hour_09();
            case 10 -> getExercise_per_hour_10();
            case 11 -> getExercise_per_hour_11();
            case 12 -> getExercise_per_hour_12();
            case 13 -> getExercise_per_hour_13();
            case 14 -> getExercise_per_hour_14();
            case 15 -> getExercise_per_hour_15();
            case 16 -> getExercise_per_hour_16();
            case 17 -> getExercise_per_hour_17();
            case 18 -> getExercise_per_hour_18();
            case 19 -> getExercise_per_hour_19();
            case 20 -> getExercise_per_hour_20();
            case 21 -> getExercise_per_hour_21();
            case 22 -> getExercise_per_hour_22();
            case 23 -> getExercise_per_hour_23();
            default -> throw new IllegalArgumentException("Invalid hour: " + hour);
        };
    }

    //Helper method
    public void setExercise_per_hour(int hour, int value) {
        switch (hour) {
            case 0 -> setExercise_per_hour_00(value);
            case 1 -> setExercise_per_hour_01(value);
            case 2 -> setExercise_per_hour_02(value);
            case 3 -> setExercise_per_hour_03(value);
            case 4 -> setExercise_per_hour_04(value);
            case 5 -> setExercise_per_hour_05(value);
            case 6 -> setExercise_per_hour_06(value);
            case 7 -> setExercise_per_hour_07(value);
            case 8 -> setExercise_per_hour_08(value);
            case 9 -> setExercise_per_hour_09(value);
            case 10 -> setExercise_per_hour_10(value);
            case 11 -> setExercise_per_hour_11(value);
            case 12 -> setExercise_per_hour_12(value);
            case 13 -> setExercise_per_hour_13(value);
            case 14 -> setExercise_per_hour_14(value);
            case 15 -> setExercise_per_hour_15(value);
            case 16 -> setExercise_per_hour_16(value);
            case 17 -> setExercise_per_hour_17(value);
            case 18 -> setExercise_per_hour_18(value);
            case 19 -> setExercise_per_hour_19(value);
            case 20 -> setExercise_per_hour_20(value);
            case 21 -> setExercise_per_hour_21(value);
            case 22 -> setExercise_per_hour_22(value);
            case 23 -> setExercise_per_hour_23(value);
            default -> throw new IllegalArgumentException("Invalid hour: " + hour);
        }
    }

    public String getBenchStreet() {
        return benchStreet;
    }

    public void setBenchStreet(String benchStreet) {
        this.benchStreet = benchStreet;
    }

    public String getBenchHouseNumber() {
        return benchHouseNumber;
    }

    public void setBenchHouseNumber(String benchHouseNumber) {
        this.benchHouseNumber = benchHouseNumber;
    }

    public String getBenchPostalCode() {
        return benchPostalCode;
    }

    public void setBenchPostalCode(String benchPostalCode) {
        this.benchPostalCode = benchPostalCode;
    }

    public Boolean getShowInApp() {
        return showInApp;
    }

    public void setShowInApp(Boolean showInApp) {
        this.showInApp = showInApp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStations() {
        return stations;
    }

    public void setStations(String stations) {
        this.stations = stations;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public Boolean getPublicAvailable() {
        return publicAvailable;
    }

    public void setPublicAvailable(Boolean publicAvailable) {
        this.publicAvailable = publicAvailable;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public MovementRoute getMovementRoute() {
        return movementRoute;
    }

    public void setMovementRoute(MovementRoute movementRoute) {
        this.movementRoute = movementRoute;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}