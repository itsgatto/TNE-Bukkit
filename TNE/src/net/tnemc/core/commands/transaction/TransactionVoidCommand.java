package net.tnemc.core.commands.transaction;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class TransactionVoidCommand extends TNECommand {

  public TransactionVoidCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "void";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.transaction.void";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Transaction.Void";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      UUID uuid = null;
      try {
        uuid = UUID.fromString(arguments[0]);
      } catch(IllegalArgumentException exception) {
        TNE.debug(exception);
      }
      if(uuid == null || !TNE.transactionManager().isValid(uuid)) {
        Message message = new Message("Messages.Transaction.Invalid");
        message.addVariable("$transaction", arguments[0]);
        message.translate(world, sender);
        return false;
      }

      TNETransaction transaction = TNE.transactionManager().get(uuid);
      if(transaction.voided()) {
        Message message = new Message("Messages.Transaction.Already");
        message.addVariable("$transaction", arguments[0]);
        message.translate(world, sender);
        return false;
      }

      boolean result = transaction.voidTransaction();
      if(!result) {
        Message message = new Message("Messages.Transaction.Unable");
        message.addVariable("$transaction", arguments[0]);
        message.translate(world, sender);
        return false;
      }
      Message message = new Message("Messages.Transaction.Voided");
      message.addVariable("$transaction", arguments[0]);
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}