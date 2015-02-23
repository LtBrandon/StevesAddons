package stevesaddons.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import stevesaddons.items.ItemSFMDrive;

import java.io.File;
import java.util.List;

public class CommandSave extends CommandDuplicator
{
    public static CommandSave instance = new CommandSave();

    @Override
    public String getCommandName()
    {
        return "save";
    }


    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return null;
    }

    @Override
    public void doCommand(ItemStack duplicator, ICommandSender sender, String[] arguments)
    {
        try
        {
            if (ItemSFMDrive.validateNBT(duplicator) && duplicator.hasTagCompound())
            {
                String name = arguments.length == 2 ? arguments[1] : sender.getCommandSenderName();
                File file = new File(DimensionManager.getCurrentSaveRootDirectory().getPath() + "\\managers\\" + name + ".nbt");
                File parent = file.getParentFile();
                if (!parent.exists() && !parent.mkdirs())
                {
                    throw new CommandException("Couldn't create dir: " + parent);
                }
                if (!file.exists()) file.createNewFile();
                NBTTagCompound tagCompound = (NBTTagCompound)duplicator.getTagCompound().copy();
                tagCompound.removeTag("x");
                tagCompound.removeTag("y");
                tagCompound.removeTag("z");
                tagCompound.setString("Author", sender.getCommandSenderName());
                CompressedStreamTools.write(stripBaseNBT(tagCompound), file);
            }
            else
            {
                throw new CommandException("stevesaddons.command.nothingToSave");
            }
        } catch (Exception e)
        {
            throw new CommandException("stevesaddons.command.saveFailed");
        }
    }
}