package uristqwerty.CraftGuide;

import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public interface CraftGuideSide
{
	public void initKeybind();
	public void preInit();
	public void reloadRecipes();
	public void openGUI(EntityPlayer player);
	public void initNetworkChannels();
	public void handlePacket(NetClientHandler handler, Packet250CustomPayload packet);
}
