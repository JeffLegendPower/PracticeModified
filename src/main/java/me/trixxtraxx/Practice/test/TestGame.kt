package me.trixxtraxx.Practice.test

import me.TrixxTraxx.InventoryAPI.Items.BetterItem
import me.TrixxTraxx.Linq.List
import me.trixxtraxx.Practice.Bungee.BungeeUtil
import me.trixxtraxx.Practice.GameLogic.Components.Components.*
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.StatComponent
import me.trixxtraxx.Practice.GameLogic.Components.Components.Stats.WinStatComponent
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Bots.BotGameLogic
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components.BedComponent
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components.ComboComponent
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components.OpponentPlaceholderComponent
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.Components.PointComponent
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelGameLogic
import me.trixxtraxx.Practice.GameLogic.DuelGameLogic.DuelSpawnComponent
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Components.GenerateLinesPlaceholder
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Components.InvisPracticeComponent
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.Components.PointComponentFFA
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.FFALogic
import me.trixxtraxx.Practice.GameLogic.FFAGameLogic.FFASpawnComponent
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.*
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers.DropToBlockinTimer
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers.DropToBreakTimer
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Components.Timers.DropToResetTimer
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloAutoScaleSpawnComponent
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloAutoscaleLogic
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.SoloGameLogic
import me.trixxtraxx.Practice.Gamemode.Game
import me.trixxtraxx.Practice.Kit.Editor.Enchants.EnchantmentEditor
import me.trixxtraxx.Practice.Kit.Editor.Item.ItemEditor
import me.trixxtraxx.Practice.Kit.Editor.KitEditor
import me.trixxtraxx.Practice.Kit.Editor.Potion.PotionEditor
import me.trixxtraxx.Practice.Kit.Kit
import me.trixxtraxx.Practice.Lobby.ItemTypes.CustomGamemodeItem
import me.trixxtraxx.Practice.Lobby.Lobby
import me.trixxtraxx.Practice.Lobby.LobbyItem
import me.trixxtraxx.Practice.Map.Components.*
import me.trixxtraxx.Practice.Map.Editor.MapEditingSession
import me.trixxtraxx.Practice.Map.Map
import me.trixxtraxx.Practice.Practice
import me.trixxtraxx.Practice.SQL.PracticePlayer
import me.trixxtraxx.Practice.SQL.SQLUtil
import me.trixxtraxx.Practice.Utils.ConfigLocation
import me.trixxtraxx.Practice.Utils.Region
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class TestGame(private val lobby: Lobby) : CommandExecutor {

    override fun onCommand(s: CommandSender, c: Command?, label: String, args: Array<String>): Boolean {
        if (label.equals("TestGame", ignoreCase = true)) {
            if (args[0].equals("Blockin", ignoreCase = true)) {
                val p = s as Player
                ///Map m = new Map(1,"Blockin1", "TestMap", new SoloSpawnCoponent(new Location(p.getWorld(), 0, 108, 0)));
                val m = SQLUtil.Instance.getMap("Blockin1")
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(
                    SoloGameLogic(),
                    List(listOf(p)),
                    k,
                    m,
                    false,
                    false
                )
                g.logic.name = "BlockinPractice"
                BreakResetComponent(g.logic, Material.BED_BLOCK)
                MapResetComponent(g.logic)
                //new SettingsComponent(g.getLogic(),Material.NETHER_STAR,ChatColor.AQUA + "Settings");
                YKillComponent(g.logic, 50)
                KillResetComponent(g.logic)
                DisconnectStopComponent(g.logic)
                val removeMaterials: List<Material> = List(Material.ANVIL, Material.BED, Material.NETHER_STAR)
                log(4, "removing materials: " + removeMaterials.size)
                DropItemComponent(g.logic, Material.ANVIL, removeMaterials, true)
                StartInventoryComponent(g.logic)
                InventoryOnResetComponent(g.logic)
                DropToBlockinTimer(g.logic)
                DropToResetTimer(g.logic)
                DropToBreakTimer(g.logic, Material.WOOL)
                DropToBreakTimer(g.logic, Material.WOOD)
                DropToBreakTimer(g.logic, Material.ENDER_STONE)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Blockin Practice",
                    "" + "\n" + ChatColor.BLUE + "Wool:         " + ChatColor.AQUA + "{WOOLTimer}" + "\n" + ChatColor.BLUE + "Wood:        " + ChatColor.AQUA + "{WOODTimer}" + "\n" + ChatColor.BLUE + "Endstone: " + ChatColor.AQUA + "{ENDER_STONETimer}" + "\n" + ChatColor.BLUE + "Blockin:     " + ChatColor.AQUA + "{BlockinTimer}" + "\n" + ChatColor.BLUE + "Finish:       " + ChatColor.AQUA + "{TotalTimer}" + "\n" + "" + "\n" + ChatColor.AQUA + "Ranked.fun" + "\n"
                )
                SQLUtil.Instance.applyComponents(m)

                /*new BedLayerComponent(m, Material.ENDER_STONE, new ConfigLocation(0, 101, 0), new ConfigLocation(1, 101, 0), 1, true);
                new BedLayerComponent(m, Material.WOOD, new ConfigLocation(0, 101, 0), new ConfigLocation(1, 101, 0), 2, false);
                new BedLayerComponent(m, Material.WOOL, new ConfigLocation(0, 101, 0), new ConfigLocation(1, 101, 0), 3, false);
                new NoMapBreakComponent(m);
                new ClearOnDropComponent(m, new Region(new Location(p.getWorld(), -5, 107, -5), new Location(p.getWorld(), 4, 112, 4)));
                new BreakRegion(m, new Region(new Location(p.getWorld(), -3, 101, 3), new Location(p.getWorld(), 4, 104, -3)), true);*/
            } else if (args[0].equals("Bridge", ignoreCase = true)) {
                val p = s as Player
                val m = Map(
                    -1,
                    "BridgeTest",
                    "BridgeTest",
                    SoloAutoScaleSpawnComponent(Location(p.world, 0.0, 100.0, 0.0, -90f, 0f))
                )
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(
                    SoloAutoscaleLogic(),
                    List(listOf(p)),
                    k,
                    m,
                    false,
                    false
                )
                g.logic.name = "BridgePractice"
                MapResetComponent(g.logic)
                PressurePlateResetComponent(g.logic)
                YKillComponent(g.logic, 90)
                KillResetComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DropItemComponent(g.logic, Material.WOOL, List(*arrayOf(Material.BED)), false)
                DropToResetTimer(g.logic)
                StartInventoryComponent(g.logic)
                InventoryOnResetComponent(g.logic)
                LeaveItemComponent(g.logic, Material.BED)
                NoHungerComponent(g.logic)
                NoKillitemDrop(g.logic)
                ResetHealComponent(g.logic)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Bridge Practice",
                    "\n" + ChatColor.BLUE + "Time:     " + ChatColor.AQUA + "{TotalTimer}" + "\n" + "\n" + ChatColor.AQUA + "Ranked.fun" + "\n"
                )
                StatComponent(g.logic)
                SuccessStat(g.logic)
                NoMapBreakComponent(m)
                AutoScaleComponent(
                    m,
                    0.0,
                    0.0,
                    20.0,
                    List<Region>(
                        Region(
                            ConfigLocation(-5.5, 90.0, -5.5, 0f, 0f),
                            ConfigLocation(5.5, 110.0, 5.5)
                        ),
                        Region(
                            ConfigLocation(20.5, 90.0, -5.5, 0f, 0f),
                            ConfigLocation(30.5, 110.0, 5.5)
                        )
                    )
                )
            } else if (args[0].equals("Jump", ignoreCase = true)) {
                val p = s as Player
                val m = Map(
                    -1,
                    "JumpWorld",
                    "JumpWorld",
                    SoloAutoScaleSpawnComponent(Location(p.world, 0.0, 100.1, 0.0, -90f, 0f))
                )
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(
                    SoloAutoscaleLogic(),
                    List(listOf(p)),
                    k,
                    m,
                    false,
                    false
                )
                g.logic.name = args.get(1)
                MapResetComponent(g.logic)
                YKillComponent(g.logic, 90)
                KillResetComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DropToResetTimer(g.logic)
                StartInventoryComponent(g.logic)
                InventoryOnResetComponent(g.logic)
                LeaveItemComponent(g.logic, Material.BED)
                NoHungerComponent(g.logic)
                NoKillitemDrop(g.logic)
                ResetHealComponent(g.logic)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "FB/TNT Jump Practice",
                    "\n" + ChatColor.BLUE + "Distance:     " + ChatColor.AQUA + "{Distance}" + "\n" + "\n" + ChatColor.AQUA + "Ranked.fun" + "\n"
                )
                StatComponent(g.logic)
                SuccessStat(g.logic)
                NoFallDamageComponent(g.logic)
                TNTFbComponent(g.logic)
                NoMapBreakComponent(m)
                AutoScaleComponent(
                    m,
                    0.0,
                    0.0,
                    20.0,
                    List<Region>(
                        Region(
                            ConfigLocation(-5.5, 90.0, -5.5, 0f, 0f),
                            ConfigLocation(5.5, 110.0, 5.5)
                        ),
                        Region(
                            ConfigLocation(20.5, 90.0, -5.5, 0f, 0f),
                            ConfigLocation(30.5, 110.0, 5.5)
                        )
                    )
                )
                JumpComponent(g.logic, Material.GOLD_BLOCK)
            } else if (args[0].equals("Invis", ignoreCase = true)) {
                val players = List<Player>()
                players.add(s as Player)
                //add Bukkit.getPlayer() for each argument after 2
                for (i in 2 until args.size) {
                    val pl = Bukkit.getPlayer(args[i])
                    if (pl != null) players.add(pl)
                }
                val spawn = FFASpawnComponent(
                    List(
                        ConfigLocation(4.5, 84.0, -76.5, 20f, -20f),
                        ConfigLocation(-2.5, 88.0, -74.5, 0f, -15f),
                        ConfigLocation(-9.5, 84.0, -75.5, -20f, -24f)
                    )
                )
                val m = Map(-1, "ToxicInvisPractice", "ToxicInvisPractice", spawn)
                val k = SQLUtil.Instance.getKit(args[1])
                //Kit k = new Kit("OneInAChamber", -1, new List<>(), -1, new HashMap<>());
                SQLUtil.Instance.applyComponents(k)
                log(3, "Starting debug Game with " + players.size + " players")
                val g = Game(FFALogic(), players, k, m, false, false)
                g.logic.name = "InvisPractice"
                NoDieComponent(g.logic)
                RespawnInventoryComponent(g.logic)
                StartInventoryComponent(g.logic)
                LeaveRemoveComponent(g.logic)
                SpectatorRespawnComponent(
                    g.logic, 100,
                    ChatColor.RED.toString() + "Respawning!",
                    ChatColor.WHITE.toString() + "You will respawn in {Timer}s!",
                    ChatColor.GREEN.toString() + "Respawned!",
                    "",
                    false
                )
                WinMessageComponent(
                    g.logic,
                    "§9-------------------------------------\n" + "\n" + "      §b{Winner}§9 won the Game!\n\n" + "          &bTop Killers:\n" + "§91. {Points1Player}§b {Points1}\n" + "§92. {Points2Player}§b {Points2}\n" + "§93. {Points3Player}§b {Points3}\n" + "\n" + "§9-------------------------------------"
                )
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Invis Practice",
                    "" + "\n" + "§9Time Left: §b{TimeLeft}\n" + "" + "\n" +
                            "§91. {Points1Player}§b {Points1}\n" + "§92. {Points2Player}§b {Points2}\n" +  //"§93. {Points3Player}§b {Points3}\n" +
                            //"§94. {Points4Player}§b {Points4}\n" +
                            "\n" + "§9You:\n" + "§9{Place}. {Name}§b {Points}" + "" + "\n" + ChatColor.AQUA + "Ranked.fun" + "\n"
                )
                StatComponent(g.logic)
                WinStatComponent(g.logic)
                NoHungerComponent(g.logic)
                NoKillitemDrop(g.logic)
                InvisPracticeComponent(g.logic, 3, 60, "InvisPractice_Attacker")
                InvisPracticeSpawnProvider(m, ConfigLocation(0.0, 100.0, 0.0, 180f, 0f))
                MapResetComponent(g.logic)
                BridgeEggComponent(g.logic, true)
                ExplosionProtectComponent(g.logic, Material.STAINED_CLAY)
                ExplosionProtectComponent(g.logic, Material.BED)
                EntityResetComponent(g.logic)
                NoChestOpenComponent(g.logic)
                TNTFbComponent(g.logic)
                InvisComponent(g.logic)
                NoMapBreakComponent(m)
                BreakRegion(
                    m,
                    Region(ConfigLocation(0.5, 84.0, -77.5), ConfigLocation(-5.5, 88.0, -70.5)),
                    true
                )
                KillHeightComponent(m, 50)
                HeightLimitComponent(m, 100)
            } else if (args[0].equals("BedFight", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[2])
                val spawn = DuelSpawnComponent(0.5, 100.0, 17.5, 180f, 0f, 0.5, 100.0, -17.5, 0f, 0f)
                val m = Map(-1, "BedFight1", "BedFight1", spawn)
                val k = SQLUtil.Instance.getKit(args[1])
                //Kit k = new Kit("", -1, new List<>(), -1, new HashMap<>());
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(p, p2), k, m, false, false)
                g.logic.name = "BedFight"
                NoDieComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DieToSpawnComponent(g.logic)
                NoKillitemDrop(g.logic)
                RespawnInventoryComponent(g.logic)
                StartInventoryComponent(g.logic)
                SpectatorRespawnComponent(
                    g.logic, 60,
                    ChatColor.RED.toString() + "Respawning!",
                    ChatColor.WHITE.toString() + "You will respawn in {Timer}s!",
                    ChatColor.GREEN.toString() + "Respawned!",
                    "",
                    false
                )
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Bed Fight",
                    "" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" + "" + "\n" + ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "Bed: " + ChatColor.AQUA + "{PlayerBed}" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + ChatColor.BLUE + "Bed: " + ChatColor.AQUA + "{OpponentBed}" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n"
                )
                WinMessageComponent(
                    g.logic,
                    "§9-------------------------------------\n" + "\n" + "      §b{Winner}§9 won the Game!\n" + "\n" + "§9-------------------------------------"
                )
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                BedComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
                NoMapBreakComponent(m)
                BreakRegion(
                    m,
                    Region(ConfigLocation(1.5, 100.0, -21.5), ConfigLocation(5.5, 102.0, -26.5)),
                    true
                )
                BreakRegion(
                    m,
                    Region(ConfigLocation(-1.5, 100.0, 21.5), ConfigLocation(-5.5, 102.0, 26.5)),
                    true
                )
                KillHeightComponent(m, 90)
                HeightLimitComponent(m, 107)
            } else if (args[0].equals("TopFight", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[2])
                val spawn = DuelSpawnComponent(34.5, 93.0, 0.5, 90f, 0f, -29.5, 93.0, 1.5, -90f, 0f)
                val m = Map(-1, "TopBridgeFight1", "TopBridgeFight1", spawn)
                val k = SQLUtil.Instance.getKit(args[1])
                //Kit k = new Kit("Sumo1", -1, new List<>(), -1, new HashMap<>());
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(p, p2), k, m, false, false)
                g.logic.name = "TopBridgeFight"
                NoDieComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                StartInventoryComponent(g.logic)
                RespawnInventoryComponent(g.logic)
                NoKillitemDrop(g.logic)
                SpawnProtComponent(
                    g.logic, 40,
                    ChatColor.BLUE.toString() + "The Game starts in " + ChatColor.AQUA + "{Timer}s",
                    ChatColor.BLUE.toString() + "The Game started, go fight!", false
                )
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Top Fight",
                    "" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" + "" + "\n" + ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + "{Points1}" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "{Points2}" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n"
                )
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                PointComponent(g.logic, 3, "⬤", true, false)
                MapResetComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
                NoMapBreakComponent(m)
                KillHeightComponent(m, 75)
                HeightLimitComponent(m, 100)
            } else if (args[0].equals("uhc", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[2])
                val spawn = DuelSpawnComponent(30.0, 100.0, 0.0, 90f, 0f, -30.0, 100.0, 0.0, -90f, 0f)
                val m = Map(-1, "Practice1", "Practice1", spawn)
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(Arrays.asList(p, p2)), k, m, false, false)
                g.logic.name = "UHC"
                YKillComponent(g.logic, 90)
                NoDieComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                StartInventoryComponent(g.logic)
                /*new SpawnProtComponent(g.getLogic(), 100,
                        ChatColor.BLUE + "The Game starts in "+ ChatColor.AQUA +"{Timer}s",
                        ChatColor.BLUE + "The Game started, go fight!");*/ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "UHC",
                    "" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" + "\n" + ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n"
                )
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                NoRegenComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
                NoMapBreakComponent(m)
            } else if (args[0].equals("Boxxing", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[2])
                val spawn = DuelSpawnComponent(30.0, 100.0, 0.0, 0f, 0f, -30.0, 100.0, 0.0, 180f, 0f)
                val m = Map(-1, "Practice1", "Practice1", spawn)
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(Arrays.asList(p, p2)), k, m, false, false)
                g.logic.name = "Boxxing"
                YKillComponent(g.logic, 90)
                NoDamageComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                NoHungerComponent(g.logic)
                StartInventoryComponent(g.logic)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Boxxing",
                    ("" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" +
                            "" + "\n" + ChatColor.BLUE + "Points:\n" + ChatColor.BLUE + "  You: " + ChatColor.AQUA + "{Points1}\n" + ChatColor.BLUE + "  Them: " + ChatColor.AQUA + "{Points2}\n" + ChatColor.BLUE + "  Combo: " + ChatColor.AQUA + "{Combo}\n\n" +
                            ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n")
                )
                WinMessageComponent(g.logic, ChatColor.BLUE.toString() + "{Winner} won the game!\n")
                ComboComponent(g.logic)
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                PointComponent(g.logic, 100, "", false, true)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
                NoMapBreakComponent(m)
            } else if (args[0].equals("NoDebuff", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[3])
                val m = SQLUtil.Instance.getMap(args[2])
                SQLUtil.Instance.applyComponents(m)
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(Arrays.asList(p, p2)), k, m, false, false)
                g.logic.name = "NoDebuff"
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                StartInventoryComponent(g.logic)
                ScoreboardComponent(
                    g.logic, ChatColor.AQUA.toString() + "NoDebuff", ("" + "\n" + ChatColor.BLUE + "Map" +
                            ":" + ChatColor.AQUA + " {MapName}" + "\n" +
                            ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n")
                )
                WinMessageComponent(g.logic).applyData("winMessage= §9-------------------------------------" + "\n       §b{Winner}§9 won the Game!" + "\n §9-------------------------------------<>\n")
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
            } else if (args[0].equals("Gapple", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[3])
                val m = SQLUtil.Instance.getMap(args[2])
                SQLUtil.Instance.applyComponents(m)
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(Arrays.asList(p, p2)), k, m, false, false)
                g.logic.name = "Gapple"
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                StartInventoryComponent(g.logic)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Gapple",
                    ("" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" +
                            ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n")
                )
                WinMessageComponent(g.logic).applyData("winMessage= §9-------------------------------------" + "\n       §b{Winner}§9 won the Game!" + "\n §9-------------------------------------<>\n")
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
            } else if (args[0].equals("Combo", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[3])
                val m = SQLUtil.Instance.getMap(args[2])
                SQLUtil.Instance.applyComponents(m)
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(Arrays.asList(p, p2)), k, m, false, false)
                g.logic.name = "Combo"
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                HitDelayComponent(g.logic)
                StartInventoryComponent(g.logic)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Combo",
                    ("" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" +
                            ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n")
                )
                WinMessageComponent(g.logic).applyData("winMessage= §9-------------------------------------" + "\n       §b{Winner}§9 won the Game!" + "\n §9-------------------------------------<>\n")
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
            } else if (args[0].equals("Classic", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[3])
                val m = SQLUtil.Instance.getMap(args[2])
                SQLUtil.Instance.applyComponents(m)
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                val g = Game(DuelGameLogic(), List(Arrays.asList(p, p2)), k, m, false, false)
                g.logic.name = "Classic"
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                StartInventoryComponent(g.logic)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Classic",
                    ("" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" +
                            ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n")
                )
                WinMessageComponent(g.logic).applyData("winMessage= §9-------------------------------------" + "\n       §b{Winner}§9 won the Game!" + "\n §9-------------------------------------<>\n")
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
            } else if (args[0].equals("sumo", ignoreCase = true)) {
                val p = s as Player
                val p2 = Bukkit.getPlayer(args[1])
                val spawn = DuelSpawnComponent(5.5, 100.0, 0.5, 90f, 0f, -4.5, 100.0, 0.5, -90f, 0f)
                val m = Map(-1, "Sumo1", "Sumo1", spawn)
                //Kit k = SQLUtil.Instance.getKit(args[2]);
                val k = Kit("Sumo1", -1, List(), HashMap())
                //SQLUtil.Instance.applyComponents(k);
                val g = Game(DuelGameLogic(), List(p, p2), k, m, false, false)
                g.logic.name = "Sumo"
                NoDieComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                StartInventoryComponent(g.logic)
                SpawnProtComponent(
                    g.logic,
                    100,
                    ChatColor.BLUE.toString() + "The Game starts in " + ChatColor.AQUA + "{Timer}s",
                    ChatColor.BLUE.toString() + "The Game started, go fight!",
                    false
                )
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Sumo",
                    "" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" + "" + "\n" + ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + "{Points1}" + "\n" + ChatColor.BLUE + "{OpponentName}" + ChatColor.AQUA + " {OpponentPing} ms" + "\n" + "{Points2}" + "\n" + "" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n"
                )
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)
                OpponentPlaceholderComponent(g.logic)
                PointComponent(g.logic, 2, "⬤", true, false)
                NoFallDamageComponent(g.logic)
                StatComponent(g.logic)
                WinStatComponent(g.logic)
                NoMapBreakComponent(m)
                KillHeightComponent(m, 97)
            } else if (args[0].equals("Chamber", ignoreCase = true)) {
                val players = List<Player>()
                players.add(s as Player)
                //add Bukkit.getPlayer() for each argument after 2
                for (i in 3 until args.size) {
                    val pl = Bukkit.getPlayer(args[i])
                    if (pl != null) players.add(pl)
                }
                var spawn: FFASpawnComponent? = null
                if (args[2].equals("8", ignoreCase = true)) {
                    spawn = FFASpawnComponent(
                        List(
                            ConfigLocation(-14.5, 95.0, 2.5, 20f, 0f),
                            ConfigLocation(-9.5, 87.0, -0.5, 16f, 0f),
                            ConfigLocation(-18.5, 80.0, 10.5, 0f, -30f),
                            ConfigLocation(-14.5, 78.0, -0.5, 10f, -15f),
                            ConfigLocation(-23.5, 87.0, 10.0, -124f, 0f),
                            ConfigLocation(-14.5, 87.0, 4.5, 80f, 0f),
                            ConfigLocation(-25.5, 93.0, 28.5, -90f, 0f),
                            ConfigLocation(-6.5, 87.0, 25.5, 140f, 0f)
                        )
                    )
                } else {
                    spawn = FFASpawnComponent(
                        List(
                            ConfigLocation(-11.5, 81.0, -43.5, 22f, -40f),
                            ConfigLocation(-26.5, 89.0, -49.5, -60f, 10f),
                            ConfigLocation(-9.5, 87.0, -56.5, 16f, 0f),
                            ConfigLocation(-22.5, 87.0, -33.5, -150f, 0f)
                        )
                    )
                }
                val m = Map(-1, "CityChamber" + args[2], "CityChamber", spawn)
                val k = SQLUtil.Instance.getKit(args[1])
                //Kit k = new Kit("OneInAChamber", -1, new List<>(), -1, new HashMap<>());
                SQLUtil.Instance.applyComponents(k)
                log(
                    3,
                    "Starting debug Game with " + players.size + " players\n" + "Kit: " + k.name + "\n" + "Map: " + m.name
                )
                val g = Game(FFALogic(), players, k, m, false, false)
                g.logic.name = "OneInAChamber"
                NoDieComponent(g.logic)
                DieToSpawnComponent(g.logic)
                RespawnInventoryComponent(g.logic)
                StartInventoryComponent(g.logic)
                GenerateLinesPlaceholder(
                    g.logic,
                    "{PointLines}",
                    "§7#{line} §b{Points{line}Player}§f {Points{line}}"
                )
                PointComponentFFA(g.logic, 20)
                KillArrowComponent(g.logic)
                DieToSpawnComponent(g.logic)
                LeaveRemoveComponent(g.logic)
                SpectatorRespawnComponent(
                    g.logic, 60,
                    ChatColor.RED.toString() + "Respawning!",
                    ChatColor.WHITE.toString() + "You will respawn in {Timer}s!",
                    ChatColor.GREEN.toString() + "Respawned!",
                    "",
                    false
                )
                WinMessageComponent(
                    g.logic,
                    "§9-------------------------------------\n" + "\n" + "      §b{Winner}§9 won the Game!\n\n" + "          §bTop Killers:\n" + "§91. {Points1Player}§b {Points1}\n" + "§92. {Points2Player}§b {Points2}\n" + "§93. {Points3Player}§b {Points3}\n" + "\n" + "§9-------------------------------------"
                )
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "One in a Chamber",
                    "\n" + "{PointLines}" + "\n" + "§9You:\n" + "§7#{Place} §b{Name}§f {Points}\n" + "\n" + ChatColor.AQUA + "Ranked.fun" + "\n"
                )
                DeathMessageComponent(g.logic, "§b{player}§9 was killed by §b{killer} §c[{Points}]")
                StatComponent(g.logic)
                WinStatComponent(g.logic)
                NoHungerComponent(g.logic)
                NoFallDamageComponent(g.logic)
                NoArrowPickupComponent(g.logic)
                NoKillitemDrop(g.logic)
                NoMapBreakComponent(m)
                ElevatorComponent(m, Material.WOOL)
            } else if (args[0].equals("Bot", ignoreCase = true)) {
                val p = s as Player
                val m = SQLUtil.Instance.getMap(args[2])
                val k = SQLUtil.Instance.getKit(args[1])
                SQLUtil.Instance.applyComponents(k)
                SQLUtil.Instance.applyComponents(m)
                val g = Game(BotGameLogic(), List(p), k, m, false, false)
                g.logic.name = "Bot"
                YKillComponent(g.logic, 90)
                NoDieComponent(g.logic)
                DisconnectStopComponent(g.logic)
                DieStopComponent(g.logic)
                StartInventoryComponent(g.logic)
                ScoreboardComponent(
                    g.logic,
                    ChatColor.AQUA.toString() + "Bot PVP",
                    "" + "\n" + ChatColor.BLUE + "Map:" + ChatColor.AQUA + " {MapName}" + "\n" + "\n" + ChatColor.BLUE + "{PlayerName}" + ChatColor.AQUA + " {PlayerPing} ms" + "\n" + ChatColor.BLUE + "Bot" + ChatColor.AQUA + " 0 ms" + "\n" + "\n" + ChatColor.BLUE + "Ranked.fun" + "\n"
                )
                PlayerPlaceholderComponent(g.logic)
                MapNamePlaceholderComponent(g.logic)

                //new StatComponent(g.getLogic());
                //new WinStatComponent(g.getLogic());
            } else if (args[0].equals("saveKit", ignoreCase = true)) {
                val p = s as Player
                val inv = p.inventory
                val defaultOrder = HashMap<Int, Int>()
                val items = List<ItemStack>()
                for (i in 0..39) {
                    val item = inv.getItem(i) ?: continue
                    defaultOrder[items.size] = i
                    items.add(item)
                }
                val k = Kit(args[1], -1, items, defaultOrder)
                SQLUtil.Instance.addKit(k)
            } else if (args[0].equals("saveMap", ignoreCase = true)) {
                val p = s as Player
                val g = Game.getGame(p)
                SQLUtil.Instance.addMap(g.logic.map)
            } else if (args[0].equals("saveGamemode", ignoreCase = true)) {
                val p = s as Player
                val g = Game.getGame(p)
                SQLUtil.Instance.addLogic(g.logic)
            } else if (args[0].equals("deleteKit", ignoreCase = true)) {
                SQLUtil.Instance.deleteKit(SQLUtil.Instance.getKit(args[1]))
            } else if (args[0].equals("loadGamemode", ignoreCase = true)) {
                val p = s as Player
                val m = SQLUtil.Instance.getMap(args[2])
                val k = SQLUtil.Instance.getKit(args[3])
                SQLUtil.Instance.applyComponents(k)
                SQLUtil.Instance.applyComponents(m)
                val ppl = List<Player>()
                ppl.add(p)
                for (i in 4 until args.size) {
                    ppl.add(Bukkit.getPlayer(args[i]))
                }
                val g = Game(SQLUtil.Instance.getLogic(args[1], false), ppl, k, m, false, false)
                SQLUtil.Instance.applyComponents(g.logic)
            } else if (args[0].equals("editMap", ignoreCase = true)) {
                if (s !is Player) return false
                val m = SQLUtil.Instance.getMap(args[1])
                SQLUtil.Instance.applyComponents(m)
                MapEditingSession.addSession(MapEditingSession(s, m))
            } else if (args[0].equals("openMapComponents", ignoreCase = true)) {
                if (s !is Player) return false
                val session = MapEditingSession.getSession(s) ?: return false
                session.openComponentGui()
            }
        } else if (label.equals("leave", ignoreCase = true)) {
            if (s !is Player) return false
            val g = Game.getGame(s)
            if (g == null) BungeeUtil.getInstance().toLobby(s) else {
                val logic = g.logic
                logic.removePlayer(s, true)
            }
        } else if (label.equals("kit", ignoreCase = true)) {
            if (s !is Player) return false
            val p = s
            val kitEditor = KitEditor.getInstance()
            if (!kitEditor.hasPlayer(p)) {
                p.sendMessage(ChatColor.RED.toString() + "Please enter a kit editing area!")
                return true
            }
            if (args.size == 0) {
                p.sendMessage("usage: /kit <potion|item|enchants>")
            } else if (args[0].equals("potion", ignoreCase = true)) {
                PotionEditor(p)
            } else if (args[0].equals("item", ignoreCase = true)) {
                ItemEditor(p)
            } else if (args[0].equals("enchants", ignoreCase = true)) {
                EnchantmentEditor(p)
            } else if (args[0].equals("test", ignoreCase = true)) {
                val stack = p.itemInHand
                val serialize = BetterItem.serialize(arrayOf(stack))
                p.sendMessage(serialize)
                p.itemInHand = BetterItem.deserialize(serialize).get(0)
            }
            return true
        } else if (label.equals("InventoryView", ignoreCase = true)) {
            if (s is Player) {
                log(4, s.getName() + " is accessing the inventory view!")
                val pp = PracticePlayer.getPlayer(s)
                if (pp == null) s.kickPlayer("§cNot found in cache") else {
                    val view = SQLUtil.Instance.getView(args[0].toInt())
                    if (view == null) s.sendMessage("§cNo View Found (Timed out)") else {
                        view.open(s)
                    }
                }
            }
        } else if (label.equals("OpenGUI", ignoreCase = true)) {
            if (s !is Player || args.size != 1) return false
            val pp = PracticePlayer.getPlayer(s) ?: return false
            pp.openBungeeInventory(args[0])
        } else if (label.equals("DuelSettings", ignoreCase = true)) {
            if (s !is Player || args.size != 0) return false
            val pp = PracticePlayer.getPlayer(s) ?: return false
            val i = lobby.items.find({ x: LobbyItem? -> x is CustomGamemodeItem }) ?: return false
            i.onClick(
                PlayerInteractEvent(
                    pp.player,
                    Action.RIGHT_CLICK_AIR,
                    ItemStack(Material.GOLD_SWORD),
                    null,
                    null
                )
            )
        } else if (label.equals("Spectate", ignoreCase = true)) {
            if (s !is Player) return false
            val pp = PracticePlayer.getPlayer(s)
            var cmd: String = "practiceSpectate"
            for (arg: String in args) {
                cmd += " $arg"
            }
            pp.executeBungeeCommand(cmd)
        }
        return false
    }

    fun log(lvl: Int, msg: String) {
        if (Practice.loglevel >= lvl) {
            if (lvl == 5) {
                Practice.Instance.logger.info("[\u001B[37mULTRA DEBUG\u001B[0m] \u001B[37m$msg")
            }
            if (lvl == 4) {
                Practice.Instance.logger.info("[\u001B[35mDEBUG\u001B[0m] \u001B[35m$msg")
            }
            if (lvl == 3) {
                Practice.Instance.logger.info("[\u001B[34mInformation\u001B[0m] \u001B[34m$msg")
            }
            if (lvl == 2) {
                Practice.Instance.logger.info("[\u001B[33mWarning\u001B[0m] \u001B[33m$msg")
            }
            if (lvl == 1) {
                Practice.Instance.logger.info("[\u001B[31mError\u001B[0m] \u001B[31m$msg")
            }
        }
    }
}