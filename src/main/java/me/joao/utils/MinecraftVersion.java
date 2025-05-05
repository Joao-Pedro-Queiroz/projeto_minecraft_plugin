package me.joao.utils;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the all the Minecraft version that are supported by the plugin.
 *
 * @author Peng1104
 * @since 2.0.0
 */

@Getter
@ToString
public enum MinecraftVersion {
    
    /**
     * The {@link MinecraftVersion} for the 1.19.3 version.
     *
     * @since 2.0.0
     */
    
    V1_19_R2(com.comphenix.protocol.utility.MinecraftVersion.FEATURE_PREVIEW_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.19 to the 1.19.2 version.
     *
     * @since 2.0.0
     */
    
    V1_19_R1(com.comphenix.protocol.utility.MinecraftVersion.WILD_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.18.2 version.
     *
     * @since 2.0.0
     */
    
    V1_18_R2(new com.comphenix.protocol.utility.MinecraftVersion(1, 18, 2)),
    
    /**
     * The {@link MinecraftVersion} for the 1.18 and the 1.18.1 version.
     *
     * @since 2.0.0
     */
    
    V1_18_R1(com.comphenix.protocol.utility.MinecraftVersion.CAVES_CLIFFS_2),
    
    /**
     * The {@link MinecraftVersion} for the 1.17 and the 1.17.1 version.
     *
     * @since 2.0.0
     */
    
    V1_17_R1(com.comphenix.protocol.utility.MinecraftVersion.CAVES_CLIFFS_1),
    
    /**
     * The {@link MinecraftVersion} for the 1.16.4 and the 1.16.5 version.
     *
     * @since 2.0.0
     */
    
    V1_16_R3(new com.comphenix.protocol.utility.MinecraftVersion(1, 16, 4)),
    
    /**
     * The {@link MinecraftVersion} for the 1.16.2 and the 1.16.3 version.
     *
     * @since 2.0.0
     */
    
    V1_16_R2(com.comphenix.protocol.utility.MinecraftVersion.NETHER_UPDATE_2),
    
    /**
     * The {@link MinecraftVersion} for the 1.16 and the 1.16.1 version.
     *
     * @since 2.0.0
     */
    
    V1_16_R1(com.comphenix.protocol.utility.MinecraftVersion.NETHER_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.15 to the 1.15.2 version.
     *
     * @since 2.0.0
     */
    
    V1_15_R1(com.comphenix.protocol.utility.MinecraftVersion.BEE_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.14 to the 1.14.4 version.
     *
     * @since 2.0.0
     */
    
    V1_14_R1(com.comphenix.protocol.utility.MinecraftVersion.VILLAGE_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.13.1 and the 1.13.2 version.
     *
     * @since 2.0.0
     */
    
    V1_13_R2(new com.comphenix.protocol.utility.MinecraftVersion(1, 13, 1)),
    
    /**
     * The {@link MinecraftVersion} for the 1.13 version.
     *
     * @since 2.0.0
     */
    
    V1_13_R1(com.comphenix.protocol.utility.MinecraftVersion.AQUATIC_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.12 to the 1.12.2 version.
     *
     * @since 2.0.0
     */
    
    V1_12_R1(com.comphenix.protocol.utility.MinecraftVersion.COLOR_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.11 to the 1.11.2 version.
     *
     * @since 2.0.0
     */
    
    V1_11_R1(com.comphenix.protocol.utility.MinecraftVersion.EXPLORATION_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.10 to the 1.10.2 version.
     *
     * @since 2.0.0
     */
    
    V1_10_R1(com.comphenix.protocol.utility.MinecraftVersion.FROSTBURN_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.9.4 version.
     *
     * @since 2.0.0
     */
    
    V1_9_R2(new com.comphenix.protocol.utility.MinecraftVersion(1, 9, 4)),
    
    /**
     * The {@link MinecraftVersion} for the 1.9 to the 1.9.2 version.
     *
     * @since 2.0.0
     */
    
    V1_9_R1(com.comphenix.protocol.utility.MinecraftVersion.COMBAT_UPDATE),
    
    /**
     * The {@link MinecraftVersion} for the 1.8.4 to the 1.8.9 version.
     *
     * @since 2.0.0
     */
    
    V1_8_R3(new com.comphenix.protocol.utility.MinecraftVersion(1, 8, 4)),
    
    /**
     * The {@link MinecraftVersion} for the 1.8.3 version.
     *
     * @since 2.0.0
     */
    
    V1_8_R2(new com.comphenix.protocol.utility.MinecraftVersion(1, 8, 3)),
    
    /**
     * The {@link MinecraftVersion} for the 1.8 to the 1.8.2 version.
     *
     * @since 2.0.0
     */
    
    V1_8_R1(com.comphenix.protocol.utility.MinecraftVersion.BOUNTIFUL_UPDATE),
    
    /**
     * Rhe {@link MinecraftVersion} that represents the current server version.
     *
     * @since 2.0.0
     */
    
    SERVER(com.comphenix.protocol.utility.MinecraftVersion.getCurrentVersion());
    
    /**
     * Determine whether the current server version is at least the given version.
     *
     * @param version - The version to compare to, if {@code null} this method will return
     * {@code false}.
     *
     * @return {@code true} if the current server version is at least the given version,
     * {@code false} otherwise.
     *
     * @since 2.0.0
     */
    
    public static boolean isAtLeast(@NotNull MinecraftVersion version) {
        return SERVER.version.compareTo(version.version) >= 0;
    }
    
    /**
     * Determine whether the current server version is exactly the given version.
     *
     * @param version - The version to compare to, if {@code null} this method will return
     * {@code false}.
     *
     * @return {@code true} if the current server version is exactly the given version,
     * {@code false} otherwise.
     *
     * @since 2.0.0
     */
    
    public static boolean isVersion(@NotNull MinecraftVersion version) {
        return SERVER.version.compareTo(version.version) == 0;
    }
    
    /**
     * Determine whether the current server version is at most the given version.
     *
     * @param version - The version to compare to, if {@code null} this method will return
     * {@code false}.
     *
     * @return {@code true} if the current server version is at most the given version,
     * {@code false} otherwise.
     *
     * @since 2.0.0
     */
    
    public static boolean isAtMost(@NotNull MinecraftVersion version) {
        return SERVER.version.compareTo(version.version) <= 0;
    }
    
    /**
     * Determine whether the current server version is in between the given versions.
     *
     * @param min - The minimum version to compare to, if {@code null} this method will return
     * {@code true} if the current server version is at most the given max version.
     * @param max - The maximum version to compare to, if {@code null} this method will return
     * {@code true} if the current server version is at least the given min version.
     *
     * @return {@code true} if the current server version is in between the given versions,
     * {@code false} otherwise.
     *
     * @since 2.0.0
     */
    
    public static boolean isInBetween(@NotNull MinecraftVersion min,
            @NotNull MinecraftVersion max) {
        return isAtLeast(min) && isAtMost(max);
    }
    
    /**
     * The underlying Minecraft protocol version.
     *
     * @since 2.0.0
     */
    
    private final com.comphenix.protocol.utility.MinecraftVersion version;
    
    /**
     * Internal constructor.
     *
     * @param version - The underlying Minecraft protocol version.
     *
     * @since 2.0.0
     */
    
    MinecraftVersion(com.comphenix.protocol.utility.MinecraftVersion version) {
        this.version = version;
    }
    
    /**
     * @return The pretty printed version name.
     *
     * @since 2.0.0
     */
    
    public String prettyPrint() {
        return version.getVersion();
    }
}