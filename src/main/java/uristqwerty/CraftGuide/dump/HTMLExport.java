package uristqwerty.CraftGuide.dump;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import uristqwerty.CraftGuide.CraftGuideLog;
import uristqwerty.CraftGuide.GuiCraftGuide;
import uristqwerty.CraftGuide.Pair;
import uristqwerty.CraftGuide.Recipe;
import uristqwerty.CraftGuide.api.CraftGuideRecipe;
import uristqwerty.CraftGuide.api.ItemSlot;
import uristqwerty.CraftGuide.api.Slot;
import uristqwerty.CraftGuide.client.ui.GuiRenderer;
import uristqwerty.CraftGuide.itemtype.ItemType;
import uristqwerty.gui_craftguide.rendering.RendererBase;
import uristqwerty.gui_craftguide.rendering.TexturedRect;

public class HTMLExport
{
	private static boolean testRequested = false;

	public static void test()
	{
		testRequested = true;
	}

	public static void maybeTest()
	{
		if(testRequested)
		{
			doTest();
			testRequested = false;
		}
	}

	public static void doTest()
	{
		Map<ItemType, List<CraftGuideRecipe>> recipes = GuiCraftGuide.getInstance().getRecipeCache().getAllRecipes();
		List<CraftGuideRecipe> tableRecipes = recipes.get(ItemType.getInstance(new ItemStack(Blocks.crafting_table)));

		try
		{
			File dir = new File("CraftGuide-export-test");
			dir.mkdirs();
			HTMLExport html = new HTMLExport(dir);

			Random rnd = new Random();
			for(int i = 0; i < 5; i++)
			{
				CraftGuideRecipe recipe = tableRecipes.get(rnd.nextInt(tableRecipes.size()));

				if(recipe instanceof Recipe)
				{
					html.writeRecipe((Recipe) recipe);
				}
			}

			html.finish();
		}
		catch (IOException | LWJGLException e)
		{
			CraftGuideLog.log(e, "Could not output recipe test", true);
		}
	}

	private File directory;
	private BufferedWriter htmlOut;

	private HashMap<Pair<TexturedRect,TexturedRect>, Integer> recipeCSSIndices = new HashMap<>();
	private int nextRecipeCSSIndex = 0;

	public HTMLExport(File dir) throws IOException
	{
		directory = dir;
		htmlOut = new BufferedWriter(new FileWriter(new File(dir, "index.html")));

		writeHeader();
	}


	private void writeHeader() throws IOException
	{
		htmlOut.write("<!DOCTYPE html><html><head><meta charset='UTF-8'><link href='recipe_export.css' rel='stylesheet'></head><body>\n");
	}


	private void writeFooter() throws IOException
	{
		htmlOut.write("</body></html>");
	}


	private void finish() throws IOException, LWJGLException
	{
		CraftGuideLog.checkGlError();
		writeFooter();
		htmlOut.close();

		Minecraft mc = Minecraft.getMinecraft();
		mc.getFramebuffer().unbindFramebuffer();

		GL11.glColorMask(true, true, true, false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, mc.displayWidth, mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);


		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int guiScale = scaledresolution.getScaleFactor();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);

		try(BufferedWriter cssOut = new BufferedWriter(new FileWriter(new File(directory, "recipe_export.css"))))
		{
			cssOut.write(".r{position:relative;top:0px;left:0px}\n");

			CraftGuideLog.checkGlError();
			for(Entry<Pair<TexturedRect, TexturedRect>, Integer> entry: recipeCSSIndices.entrySet())
			{
				String prefix = "r" + entry.getValue();
				TexturedRect bg = entry.getKey().first;
				TexturedRect bgSel = entry.getKey().second;

				cssOut.write("." + prefix + "{width:" + (bg.width*guiScale) + "px;height:" + (bg.height*guiScale) + "px;background:url('" + prefix + "-bg.png');}\n");
				cssOut.write("." + prefix + ":hover{width:" + (bgSel.width*guiScale) + "px;height:" + (bgSel.height*guiScale) + "px;background:url('" + prefix + "-bgh.png');}\n");

				renderToImage(bg, new File(directory, prefix + "-bg.png"), scaledresolution.getScaleFactor());
				renderToImage(bgSel, new File(directory, prefix + "-bgh.png"), scaledresolution.getScaleFactor());
			}

		}

		GL11.glDepthMask(true);
		GL11.glColorMask(true, true, true, true);
		mc.getFramebuffer().bindFramebuffer(true);
		CraftGuideLog.checkGlError();
	}

	private void renderToImage(TexturedRect tex, File file, int guiScale) throws IOException, LWJGLException
	{
		CraftGuideLog.checkGlError();
		int windowHeight = Minecraft.getMinecraft().displayHeight;
		int width = tex.width * guiScale;
		int height = tex.height * guiScale;
		ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
		GuiRenderer renderer = (GuiRenderer)RendererBase.instance;

//		GL11.glDisable(GL11.GL_STENCIL_TEST);

		CraftGuideLog.checkGlError();
		tex.render(renderer, -tex.x, -tex.y);
		CraftGuideLog.checkGlError();

		boolean front = false;
		if(front )
		{
			GL11.glReadBuffer(GL11.GL_FRONT);
			Display.swapBuffers();
			CraftGuideLog.checkGlError();
		}
		else
		{
			GL11.glReadBuffer(GL11.GL_BACK);
		}

		GL11.glFinish();

		GL11.glReadPixels(0, windowHeight - height, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		CraftGuideLog.checkGlError();

		BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);

		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				int rgba = buffer.getInt((x + (height - y - 1)*width) * 4);
				int argb = (rgba >>> 8) | ((0xff ^ rgba) << 24);
				frame.setRGB(x, y, argb);
			}
		}

		ImageWriter writer = findPngWriter();

		FileImageOutputStream output = new FileImageOutputStream(file);
		writer.setOutput(output);
		writer.write(new IIOImage(frame, null, null));
		output.close();
		writer.dispose();
		CraftGuideLog.checkGlError();
	}

	private void writeRecipe(Recipe recipe) throws IOException
	{
		htmlOut.write("<div class='r " + recipeCSS(recipe) + "'>");
		Slot[] slots = recipe.getSlotData();
		Object[] items = recipe.getItems();

		for(int i = 0; i < slots.length; i++)
		{
			if(slots[i] instanceof ItemSlot)
			{
				ItemSlot slot = (ItemSlot)slots[i];
				ItemStack item = slotItem(slot, items[i]);
				String divClasses = (slot.drawBackground? "slot " : "") + "i " + itemCSS(item);
				htmlOut.write("<div class='" + divClasses + "' style='top:" + slot.y + "px;left:" + slot.x + "px'></div>");
			}
		}
		htmlOut.write("</div>\n");
	}


	private String itemCSS(ItemStack item)
	{
		// TODO Auto-generated method stub
		return "ni";
	}


	private ItemStack slotItem(ItemSlot slot, Object object)
	{
		// TODO Auto-generated method stub
		return null;
	}


	private String recipeCSS(Recipe recipe)
	{
		Pair<TexturedRect, TexturedRect> key = new Pair<>((TexturedRect)recipe.background, (TexturedRect)recipe.backgroundSelected);
		Integer index = recipeCSSIndices.get(key);

		if(index == null)
		{
			index = nextRecipeCSSIndex++;
			recipeCSSIndices.put(key, index);
		}

		return "r" + index;
	}


	public static void outputItemIcon(ItemStack stack, File dest)
	{
//		IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(stack, ItemRenderType.INVENTORY);
//		Item item = stack.getItem();
//		Block block = Block.getBlockFromItem(item);
//		boolean renderAsBlock = stack.getItemSpriteNumber() == 0 && item instanceof ItemBlock && RenderBlocks.renderItemIn3d(block.getRenderType());
//		IIcon icon = stack.getIconIndex();
//
//		if(renderer == null && !renderAsBlock && icon instanceof TextureAtlasSprite && ((TextureAtlasSprite)icon).getFrameCount() > 1)
//		{
//			// TODO: Read frame data from TextureAtlasSprite, rescale, output GIF
//		}
//		else
//		{
//			// TODO: Render to texture, copy pixels into spritesheet PNG.
//		}
	}

//	public static void old_test()
//	{
//		try
//		{
//
////			TextureMap textureMap = (TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture);
////			TextureAtlasSprite test_icon = (TextureAtlasSprite)textureMap.registerIcon("craftguide:palette-extension-test-icon"); //"craftguide:palette-extension-test-icon"
////			Minecraft.getMinecraft().refreshResources();
//			TextureAtlasSprite test_icon = (TextureAtlasSprite) CraftGuide.itemCraftGuide.getIconFromDamage(0);
//			//test_icon = (TextureAtlasSprite) Blocks.flowing_lava.getIcon(0, 0);
//			int frames = test_icon.getFrameCount();
//
//			//GL11.glGetTexImage(target, level, format, type, pixels);
//
//			if(frames > 0)
//			{
//				ImageWriter writer = findGifWriter();
//				FileImageOutputStream output = new FileImageOutputStream(new File("test.gif"));
//				writer.setOutput(output);
//				writer.prepareWriteSequence(null);
//
//				CraftGuideLog.log("Writing animated icon " + test_icon.getIconName() + ": " + test_icon.getIconWidth() + "*" + test_icon.getIconHeight() + "px with " + frames + " frames");
//				for(int i = 0; i < frames; i++)
//				{
//					int width = test_icon.getIconWidth();
//					int height = test_icon.getIconHeight();
//					BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//					int[][] pix = test_icon.getFrameTextureData(i);
//					for(int x = 0; x < width; x++)
//					{
//						for(int y = 0; y < height; y++)
//						{
//							frame.setRGB(x, y, pix[0][x*width+y]);
//						}
//					}
//					writer.writeToSequence(new IIOImage(frame, null, null), null);
//				}
//
//				writer.endWriteSequence();
//				writer.dispose();
//			}
//			else
//			{
//				CraftGuideLog.log("Writing icon " + test_icon.getIconName() + ": " + test_icon.getIconWidth() + "*" + test_icon.getIconHeight() + "px");
//				int width = test_icon.getIconWidth();
//				int height = test_icon.getIconHeight();
//				BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//			}
//
//		}
//		catch (IOException e)
//		{
//			CraftGuideLog.log(e);
//		}
//	}
//
	@SuppressWarnings("unused")
	private static ImageWriter findGifWriter()
	{
		Iterator<ImageWriter> i = ImageIO.getImageWritersByFormatName("gif");

		while(i.hasNext())
		{
			ImageWriter w = i.next();
			if(w.canWriteSequence())
				return w;
		}

		return null;
	}

	private static ImageWriter findPngWriter()
	{
		Iterator<ImageWriter> i = ImageIO.getImageWritersByFormatName("png");

		while(i.hasNext())
		{
			ImageWriter w = i.next();
			return w;
		}

		return null;
	}
}
