package org.hmmbo.hmmmkits.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
      if(p.isOp()){
          if(args.length==1)
          {
              return StringUtil.copyPartialMatches(args[0], Arrays.asList("create","help","delete"), new ArrayList<>());
          }else if(args.length==2) {
              if (args[0].equals("create")) {
                  return StringUtil.copyPartialMatches(args[1], Arrays.asList("name"), new ArrayList<>());
              }
              if(args[0].equals("delete")){
                  File f = new File("config.yml");
                  return StringUtil.copyPartialMatches(args[1],Arrays.asList("kit_name"),new ArrayList<>());
              }
          }else if(args.length==3) {
              if (args[0].equals("create")) {
                  return StringUtil.copyPartialMatches(args[2], Arrays.asList("display_name"), new ArrayList<>());
              }
          }
          else if(args.length==4) {
              if (args[0].equals("create")) {
                  return StringUtil.copyPartialMatches(args[3], Arrays.asList("Cooldown(Secs)"), new ArrayList<>());
              }
          }
          else if(args.length==5) {
              if (args[0].equals("create")) {
                  return StringUtil.copyPartialMatches(args[4], Arrays.asList("Weight"), new ArrayList<>());
              }
          }
      }

        return null;
    }
}
