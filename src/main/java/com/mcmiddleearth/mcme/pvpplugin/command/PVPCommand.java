package com.mcmiddleearth.mcme.pvpplugin.command;

import com.google.common.base.Joiner;
import com.mcmiddleearth.mcme.pvpplugin.PVPPlugin;
import com.mcmiddleearth.mcme.pvpplugin.command.handler.AbstractCommandHandler;
import com.mcmiddleearth.mcme.pvpplugin.command.node.HelpfulNode;
import com.mcmiddleearth.mcme.pvpplugin.util.Style;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PVPCommand extends Command {

    private final CommandDispatcher<CommandSender> commandDispatcher;

    public PVPCommand(CommandDispatcher<CommandSender> commandDispatcher, AbstractCommandHandler handler) {
        super(handler.getCommand());
        this.commandDispatcher = commandDispatcher;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
//CommandNode<CommandSender> node = dispatcher.getRoot();
//printTree(node);
            String message = String.format("%s %s", getName(), Joiner.on(' ').join(args)).trim();
            ParseResults<CommandSender> result = commandDispatcher.parse(message, sender);
//Logger.getGlobal().info("nodes "+result.getContext().getNodes().size());
//Logger.getGlobal().info("Parsed");
            result.getExceptions().entrySet().stream()
                    .findFirst().ifPresent(error -> sender.sendMessage(new ComponentBuilder(error.getValue().getMessage())
                    .color(Style.ERROR).create()));
            if(result.getExceptions().isEmpty()) {
                if(result.getContext().getNodes().size() > 0
                        && (result.getContext().getCommand()==null
                        || result.getContext().getRange().getEnd() < result.getReader().getString().length())) {
                    //check for possible child nodes to collect suggestions and bake better error message
                    ComponentBuilder helpMessage;
                    boolean help = false;
                    String parsedCommand = "/" + result.getReader().getString()
                            .substring(0, result.getContext().getRange().getEnd());
                    if(result.getReader().getRemaining().trim().equals("help")){
                        helpMessage = new ComponentBuilder("Help for command "+parsedCommand+":").color(Style.INFO);
                        help = true;
                    } else {
                        helpMessage = new ComponentBuilder("Invalid command syntax.").color(Style.ERROR);
                    }
                    CommandNode<CommandSender> parsedNode = result.getContext().getNodes().get(result.getContext().getNodes().size() - 1).getNode();
//Logger.getGlobal().info("Parsed Node:");
//printTree(parsedNode);
                    Collection<CommandNode<CommandSender>> children = (result.getContext().getNodes().isEmpty()?new ArrayList<>():parsedNode.getChildren()
                            .stream().filter(node -> node.canUse(result.getContext().getSource())).collect(Collectors.toList()));
                    Map<CommandNode<CommandSender>,String> use = commandDispatcher.getSmartUsage(parsedNode,result.getContext().getSource());
                    if (children.isEmpty()) {
                        if (result.getContext().getCommand() == null) {
                            helpMessage.append(" Maybe you don't have permission.");
                        } else if(!help) {
                            helpMessage.append(" Maybe you want to do:\n").append(parsedCommand).color(Style.INFO);
                        }
                    } else {
                        if(!help) {
                            helpMessage.append(" Maybe you want to do:");
                        }
                        for(Map.Entry<CommandNode<CommandSender>,String> entry: use.entrySet()) {
                            String usageMessage = "";
                            helpMessage.append("\n").color(Style.INFO);
                            String[] visitedNodes = parsedCommand.split(" ");
                            Iterator<ParsedCommandNode<CommandSender>> iterator = result.getContext().getNodes().listIterator();
                            for (String visitedNode : visitedNodes) {
                                helpMessage.append(" "+visitedNode);
                                ParsedCommandNode<CommandSender> node = iterator.next();
//Logger.getGlobal().info("Visited Node:");
//printTree(node.getNode());
                                helpMessage.color((node.getNode() instanceof LiteralCommandNode?Style.LITERAL:Style.ARGUMENT));
                                if ((node.getNode() instanceof HelpfulNode)) {
                                    helpMessage.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            new Text(new ComponentBuilder(((HelpfulNode) node.getNode()).getTooltip())
                                                    .color(Style.TOOLTIP).create())));
                                    if(!((HelpfulNode) node.getNode()).getHelpText().equals("")) {
                                        usageMessage = ((HelpfulNode) node.getNode()).getHelpText();
                                    }
                                } else {
                                    helpMessage.event((HoverEvent)null);
                                }
                            }
                            String[] possibleNodes = entry.getValue().replace('|', ' ').split(" ");
                            CommandNode<CommandSender> node = parsedNode;
                            CommandNode<CommandSender> lastNode = parsedNode;
                            for(String possibleNode: possibleNodes) {
//Logger.getLogger(ModerationPluginCommand.class.getSimpleName()).info("possible node "+possibleNode);
                                helpMessage.append(" "+possibleNode);
                                CommandNode<CommandSender> temp = node;
                                node = findDirectChild(node, possibleNode.replaceAll("[()\\[\\]<>]",""));
                                if(node==null) {
                                    node = findDirectChild(lastNode, possibleNode.replaceAll("[()\\[\\]<>]",""));
                                } else {
                                    lastNode = temp;
                                }
//Logger.getGlobal().info("possible Node:");
//printTree(node);
                                helpMessage.color((node instanceof LiteralCommandNode?Style.LITERAL:Style.ARGUMENT));
                                if ((node instanceof HelpfulNode)) {
                                    helpMessage.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            new Text(new ComponentBuilder(((HelpfulNode) node).getTooltip())
                                                    .color(Style.TOOLTIP).create())));
                                    if(!((HelpfulNode) node).getHelpText().equals("")) {
                                        usageMessage = ((HelpfulNode) node).getHelpText();
                                    }
                                } else {
                                    helpMessage.event((HoverEvent) null);
                                }
                            }
                            if(!usageMessage.equals("")) {
                                helpMessage.append(" : "+usageMessage).color(Style.HELP)
                                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text(new ComponentBuilder().create())));
                            }
                        }
                    }
                    PVPPlugin.sendInfo(sender,helpMessage);
                } else if(result.getContext().getCommand() == null) {
                    PVPPlugin.sendError(sender,new ComponentBuilder("Invalid command. Maybe you don't have permission.")
                            .color(ChatColor.RED));
                } else {
                    commandDispatcher.execute(result);
                }
            }
        } catch (CommandSyntaxException e) {
            PVPPlugin.sendError(sender,new ComponentBuilder("Internal command parser exception!")
                    .color(ChatColor.RED));
        }
    }

    public void onTabComplete(TabCompleteEvent event) {
        if (event.getSender() instanceof CommandSender) {
            try {
                ParseResults<CommandSender> result = commandDispatcher.parse(event.getCursor().substring(1), (ProxiedPlayer) event.getSender());
                if(result.getContext().getNodes().isEmpty()) {
                    return;
                }
                List<Suggestion> completionSuggestions
                        = commandDispatcher.getCompletionSuggestions(result).get().getList();
                if(completionSuggestions.isEmpty()) {
                    event.setCancelled(true);
                } else {
                    event.getSuggestions().addAll(completionSuggestions.stream().map(Suggestion::getText).collect(Collectors.toList()));
                }
            } catch (InterruptedException | ExecutionException e) {
                Logger.getLogger(PVPPlugin.class.getSimpleName()).log(Level.WARNING,"Command tab complete error.",e);
            }
        }
    }

    private CommandNode<CommandSender> findDirectChild(CommandNode<CommandSender> root, String name) {
//Logger.getLogger(ModerationPluginCommand.class.getSimpleName()).info("find node "+name);
        //if(root.getName().equals(name)) {
        //    return root;
        //} else {
        for(CommandNode<CommandSender> node: root.getChildren()) {
            //CommandNode<CommandSender> found = findNode(node,name);
            if(node.getName().equals(name)) {//found != null) {
                return node;//found;
            }
        }
        //}
        return null;
    }

    private void printTree(CommandNode<CommandSender> node) {
        Logger log = Logger.getLogger(PVPPlugin.class.getSimpleName());
        log.info(printNode(node, "", "  "));
    }

    private String printNode(CommandNode<CommandSender> node, String message, String indentation) {
        message = message + indentation+node.getClass().getSimpleName()+" "+node.getName()
                +"\n"+indentation+"-use: "+node.getUsageText();
        if(node instanceof HelpfulNode) {
            message = message
                    +"\n"+indentation+"-help: "+((HelpfulNode)node).getHelpText()
                    +"\n"+indentation+"-tool: "+((HelpfulNode)node).getTooltip();
        }
        for(CommandNode<CommandSender> child: node.getChildren()) {
            message = printNode(child,message+"\n",indentation+"    ");
        }
        return message;
    }
}
