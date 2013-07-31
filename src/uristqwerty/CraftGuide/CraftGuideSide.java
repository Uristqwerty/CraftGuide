package uristqwerty.CraftGuide;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet250CustomPayload;

public interface CraftGuideSide
{
	public void initKeybind();
	public void preInit();
	public void reloadRecipes();
	public void openGUI(EntityPlayer player);
	public void initNetworkChannels();
	public void handlePacket(NetClientHandler handler, Packet250CustomPayload packet);

	/* Ensure that Tessellator isn't drawing. More to prevent an otherwise nonfatal
	 *  rendering errors from crashing Minecraft entirely. Better for CraftGuide to
	 *  be unusable (invisible items, offset stuff, other graphical glitches), than
	 *  to dump the player back to windows. Since keyboard input not affected by
	 *  render issues, you can always exit the GUI and continue playing without it.
	 */
	public void stopTessellating();
}
