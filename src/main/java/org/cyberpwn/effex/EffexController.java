package org.cyberpwn.effex;

import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.cyberpwn.effex.effect.AmbientEffect;
import org.cyberpwn.effex.effect.VampiricEffect;
import org.cyberpwn.effex.enchantments.AlchemistEnchantment;
import org.cyberpwn.effex.enchantments.AntiGravityEnchantment;
import org.cyberpwn.effex.enchantments.BeheadingEnchantment;
import org.cyberpwn.effex.enchantments.BlastEnchantment;
import org.cyberpwn.effex.enchantments.BlessingEnchantment;
import org.cyberpwn.effex.enchantments.ColdBloodedEnchantment;
import org.cyberpwn.effex.enchantments.DeflectEnchantment;
import org.cyberpwn.effex.enchantments.DefusedEnchantment;
import org.cyberpwn.effex.enchantments.DemolitionEnchantment;
import org.cyberpwn.effex.enchantments.DevilsGraceEnchantment;
import org.cyberpwn.effex.enchantments.EarthquakeEnchantment;
import org.cyberpwn.effex.enchantments.EmpowermentEnchantment;
import org.cyberpwn.effex.enchantments.EnderEnchantment;
import org.cyberpwn.effex.enchantments.ForgeEnchantment;
import org.cyberpwn.effex.enchantments.FrostEnchantment;
import org.cyberpwn.effex.enchantments.HeatSeekerEnchantment;
import org.cyberpwn.effex.enchantments.InfernoEnchantment;
import org.cyberpwn.effex.enchantments.LongShotEnchantment;
import org.cyberpwn.effex.enchantments.MagnetEnchantment;
import org.cyberpwn.effex.enchantments.MossEnchantment;
import org.cyberpwn.effex.enchantments.NightmareEnchantment;
import org.cyberpwn.effex.enchantments.NocturnalEnchantment;
import org.cyberpwn.effex.enchantments.PoisonEnchantment;
import org.cyberpwn.effex.enchantments.RageEnchantment;
import org.cyberpwn.effex.enchantments.ReinforcedEnchantment;
import org.cyberpwn.effex.enchantments.ShockEnchantment;
import org.cyberpwn.effex.enchantments.SnareEnchantment;
import org.cyberpwn.effex.enchantments.SoulEruptionEnchantment;
import org.cyberpwn.effex.enchantments.SoulStealerEnchantment;
import org.cyberpwn.effex.enchantments.SoullessEnchantment;
import org.cyberpwn.effex.enchantments.SpringEnchantment;
import org.cyberpwn.effex.enchantments.StaminaEnchantment;
import org.cyberpwn.effex.enchantments.StickyEnchantment;
import org.cyberpwn.effex.enchantments.VampiricEnchantment;
import org.phantomapi.Phantom;
import org.phantomapi.clust.Comment;
import org.phantomapi.clust.ConfigurableController;
import org.phantomapi.clust.Keyed;
import org.phantomapi.command.Command;
import org.phantomapi.command.CommandAlias;
import org.phantomapi.command.CommandFilter.Permission;
import org.phantomapi.command.CommandFilter.PlayerOnly;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.ControllerMessage;
import org.phantomapi.construct.Ticked;
import org.phantomapi.event.PlayerKillPlayerEvent;
import org.phantomapi.event.PlayerMoveBlockEvent;
import org.phantomapi.event.TNTDispenseEvent;
import org.phantomapi.event.TNTPrimeEvent;
import org.phantomapi.gui.Click;
import org.phantomapi.gui.Element;
import org.phantomapi.gui.Guis;
import org.phantomapi.gui.PhantomElement;
import org.phantomapi.gui.PhantomWindow;
import org.phantomapi.gui.Slot;
import org.phantomapi.gui.Window;
import org.phantomapi.inventory.PhantomInventory;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSound;
import org.phantomapi.nms.NMSX;
import org.phantomapi.physics.VectorMath;
import org.phantomapi.stack.Stack;
import org.phantomapi.stack.StackedInventory;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.util.C;
import org.phantomapi.util.F;
import org.phantomapi.util.M;
import org.phantomapi.util.P;
import org.phantomapi.util.Range;
import org.phantomapi.vfx.LineParticleManipulator;
import org.phantomapi.vfx.ParticleEffect;
import org.phantomapi.world.Area;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.PE;
import org.phantomapi.world.W;
import com.earth2me.essentials.craftbukkit.SetExpFix;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import net.md_5.bungee.api.ChatColor;

@Ticked(1)
public class EffexController extends ConfigurableController
{
	private GMap<Location, GMap<String, Integer>> enchanted;
	private GMap<Player, Float> affected;
	private GList<CustomEnchantment> enchantments;
	public static EffexController inst;
	private NPCController npcController;
	
	@Comment("The Cost multipler for the intensity. Remember the intensity is judged by\nBASE X LEVEL.\nThe cost is INTENSITY X MULTIPLIER")
	@Keyed("math.cost-multiplier")
	public double costMultiple = 1846;
	
	@Keyed("math.tiers")
	public GList<String> tiers = new GList<String>().qadd("apprentice").qadd("novice").qadd("scholar").qadd("expert").qadd("master");
	
	private GMap<String, GList<Enchant>> tierSet;
	private GMap<String, Range> ranged;
	private GMap<String, Double> cost;
	//TODO ybcdfgfghsgh
	//private GList<Player> ignored;
	
	public EffexController(Controllable parentController)
	{
		super(parentController, "config");
		
		inst = this;
		enchanted = new GMap<Location, GMap<String, Integer>>();
		affected = new GMap<Player, Float>();
		enchantments = new GList<CustomEnchantment>();
		npcController = new NPCController(this);
		tierSet = new GMap<String, GList<Enchant>>();
		ranged = new GMap<String, Range>();
		cost = new GMap<String, Double>();
		//ignored = new GList<Player>();
		//TODO asfdsadfasfd
		
		register(npcController);
	}
	
	@Override
	public void onStart()
	{
		enchantments.add(new ShockEnchantment());
		enchantments.add(new FrostEnchantment());
		enchantments.add(new InfernoEnchantment());
		enchantments.add(new PoisonEnchantment());
		enchantments.add(new VampiricEnchantment());
		enchantments.add(new DeflectEnchantment());
		enchantments.add(new SnareEnchantment());
		enchantments.add(new StickyEnchantment());
		enchantments.add(new BlastEnchantment());
		enchantments.add(new DefusedEnchantment());
		enchantments.add(new RageEnchantment());
		enchantments.add(new NightmareEnchantment());
		enchantments.add(new EarthquakeEnchantment());
		enchantments.add(new BeheadingEnchantment());
		enchantments.add(new ReinforcedEnchantment());
		enchantments.add(new LongShotEnchantment());
		enchantments.add(new SoulEruptionEnchantment());
		enchantments.add(new SoulStealerEnchantment());
		enchantments.add(new EmpowermentEnchantment());
		enchantments.add(new BlessingEnchantment());
		enchantments.add(new DemolitionEnchantment());
		enchantments.add(new SoullessEnchantment());
		enchantments.add(new DevilsGraceEnchantment());
		enchantments.add(new ColdBloodedEnchantment());
		enchantments.add(new ForgeEnchantment());
		enchantments.add(new AntiGravityEnchantment());
		enchantments.add(new AlchemistEnchantment());
		enchantments.add(new StaminaEnchantment());
		enchantments.add(new MossEnchantment());
		enchantments.add(new NocturnalEnchantment());
		enchantments.add(new HeatSeekerEnchantment());
		enchantments.add(new SpringEnchantment());
		enchantments.add(new EnderEnchantment());
		enchantments.add(new MagnetEnchantment());
		
		for(CustomEnchantment i : enchantments)
		{
			EnchantmentAPI.registerCustomEnchantment(i);
			getConfiguration().set("enchantment." + i.getClass().getSimpleName().toLowerCase().replaceAll("enchantment", ""), ((Enchanted) i).getIntensity(1));
		}
		
		loadCluster(this);
		saveCluster(this);
		
		for(CustomEnchantment i : enchantments)
		{
			Enchanted e = (Enchanted) i;
			e.setPow(getConfiguration().getDouble("enchantment." + i.getClass().getSimpleName().toLowerCase().replaceAll("enchantment", "")));
		}
		
		buildTiers();
	}
	
	public void buildTiers()
	{
		GMap<String, GList<Enchant>> cloned = new GMap<String, GList<Enchant>>();
		GMap<Enchant, Double> mapping = new GMap<Enchant, Double>();
		double ran = 0;
		double max = 0;
		double seg = 0;
		
		for(CustomEnchantment i : enchantments)
		{
			Enchanted e = (Enchanted) i;
			double power = e.getIntensity(1);
			
			for(int j = 0; j < i.getMaxLevel(); j++)
			{
				int level = j + 1;
				Enchant ex = new Enchant(e, level);
				mapping.put(ex, power * level);
				
				if(power * level > max)
				{
					max = power * level;
				}
			}
		}
		
		seg = max / tiers.size();
		
		for(String i : tiers)
		{
			Range r = new Range(ran, ran + seg);
			cloned.put(i, new GList<Enchant>());
			ranged.put(i, r);
			double cost = 0;
			int itr = 0;
			
			for(Enchant j : mapping.k())
			{
				if(r.isWithin(mapping.get(j)))
				{
					cloned.get(i).add(j);
					cost += j.getLevel() * j.getE().getIntensity(1);
					itr++;
				}
			}
			
			this.cost.put(i, cost / itr);
			ran += seg;
		}
		
		tierSet = cloned;
	}
	
	@Command("enchantments")
	@CommandAlias("enchants")
	public void onEnchants(PhantomSender sender, PhantomCommand command)
	{
		if(sender.isPlayer())
		{
			Window w = new PhantomWindow(C.LIGHT_PURPLE + "Enchantments", sender.getPlayer());
			GList<Slot> s = Guis.sortLTR(Guis.getCentered(tiers.size(), 2));
			
			for(String i : tiers)
			{
				Element e = new PhantomElement(new ItemStack(Material.ENCHANTED_BOOK), s.pop())
				{
					@Override
					public void onClick(Player p, Click c, Window w)
					{
						openRevealWindow(p, i);
					}
				};
				
				int cost = (int) (costMultiple * this.cost.get(i));
				e.setTitle(C.LIGHT_PURPLE + StringUtils.capitalize(i.toLowerCase()) + " Enchantments");
				e.addText(C.DARK_PURPLE + "Left Click to Randomly Purchase one");
				e.addText(C.DARK_PURPLE + "Right Click to see all possibilities");
				e.addText(C.GREEN + "Costs " + F.f(cost) + " XP");
				w.addElement(e);
			}
			
			w.setViewport(3);
			w.setBackground(new PhantomElement(new ItemStack(Material.STAINED_GLASS_PANE), new Slot(0)).setMetadata((byte) 15));
			w.open();
		}
	}
	
	public void openEnchantWindow(Player p)
	{
		Window w = new PhantomWindow(C.LIGHT_PURPLE + " Enchantments", p);
		w.setViewport(3);
		int ind = -2;
		
		for(ETag i : ETag.values())
		{
			ETag tag = i;
			String n = StringUtils.capitalize(i.toString().toLowerCase().replaceAll("_", " "));
			
			Element e = new PhantomElement(new ItemStack(Material.ENCHANTED_BOOK), new Slot(ind, 2))
			{
				@Override
				public void onClick(Player p, Click c, Window w)
				{
					Window wx = new PhantomWindow(C.DARK_PURPLE + n + " Enchantments", p);
					int ix = 0;
					
					for(Enchanted i : EffexController.inst.getEnchantments())
					{
						if(new GList<ETag>(i.getTags()).contains(tag))
						{
							CustomEnchantment en = (CustomEnchantment) i;
							String name = i.getClass().getSimpleName().replaceAll("Enchantment", "");
							Element ex = new PhantomElement(new ItemStack(Material.ENCHANTED_BOOK), new Slot(ix))
							{
								@Override
								public void onClick(Player p, Click c, Window w)
								{
									Window wz = new PhantomWindow(C.DARK_PURPLE + name, p);
									int ixz = 0;
									
									for(int i = 0; i < en.getMaxLevel(); i++)
									{
										int level = i + 1;
										
										Element ez = new PhantomElement(new ItemStack(Material.ENCHANTED_BOOK), new Slot(ixz))
										{
											@Override
											public void onClick(Player p, Click c, Window w)
											{
												int cost = (int) EffexController.inst.getCost((Enchanted) en, level);
												
												if(SetExpFix.getTotalExperience(p) >= cost)
												{
													if(new PhantomInventory(p.getInventory()).hasSpace())
													{
														ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
														en.addToItem(is, level);
														ItemMeta im = is.getItemMeta();
														im.setDisplayName(C.LIGHT_PURPLE + name + " " + M.toRoman(level));
														p.getInventory().addItem(is);
														SetExpFix.setTotalExperience(p, SetExpFix.getTotalExperience(p) - cost);
													}
													
													else
													{
														p.sendMessage(C.RED + "No Space!");
													}
												}
												
												else
												{
													p.sendMessage(C.RED + "You only have " + F.f(SetExpFix.getTotalExperience(p)) + " XP");
												}
											}
										};
										
										ez.setTitle(C.LIGHT_PURPLE + name + " " + M.toRoman(level));
										ez.addText(C.LIGHT_PURPLE + "Click to aquire for " + C.GREEN + F.f((int) EffexController.inst.getCost((Enchanted) en, level)) + " XP");
										ez.addText(C.RED + F.pc(((Enchanted) en).getIntensity(level) * (double) level) + " More Intense");
										wz.addElement(ez);
										ixz++;
									}
									
									wz.setBackground(new PhantomElement(new ItemStack(Material.STAINED_GLASS_PANE), new Slot(0)).setMetadata((byte) 15));
									wz.open();
								}
							};
							
							ex.setTitle(C.LIGHT_PURPLE + name);
							ex.addText(C.DARK_PURPLE + en.getDescription());
							wx.addElement(ex);
							ix++;
						}
					}
					
					wx.setBackground(new PhantomElement(new ItemStack(Material.STAINED_GLASS_PANE), new Slot(0)).setMetadata((byte) 15));
					wx.open();
				}
			};
			
			e.setTitle(C.LIGHT_PURPLE + n + " Enchantments");
			e.addText(C.DARK_PURPLE + "Click to buy " + n + " Enchantments.");
			w.addElement(e);
			ind++;
		}
		
		w.setBackground(new PhantomElement(new ItemStack(Material.STAINED_GLASS_PANE), new Slot(0)).setMetadata((byte) 15));
		w.open();
	}
	
	public void openWindow(Player p)
	{
		Window w = new PhantomWindow(C.LIGHT_PURPLE + "Enchantments", p);
		GList<Slot> s = Guis.sortLTR(Guis.getCentered(tiers.size(), 2));
		
		for(String i : tiers)
		{
			Element e = new PhantomElement(new ItemStack(Material.ENCHANTED_BOOK), s.pop())
			{
				@Override
				public void onClick(Player p, Click c, Window w)
				{
					if(c.equals(Click.LEFT))
					{
						openTierWindow(p, i);
					}
					
					else if(c.equals(Click.SHIFT_RIGHT))
					{
						openSuperRevealWindow(p, i);
					}
					
					else
					{
						openRevealWindow(p, i);
					}
				}
			};
			
			int cost = (int) (costMultiple * this.cost.get(i));
			e.setTitle(C.LIGHT_PURPLE + StringUtils.capitalize(i.toLowerCase()) + " Enchantments");
			e.addText(C.DARK_PURPLE + "Left Click to Randomly Purchase one");
			e.addText(C.DARK_PURPLE + "Right Click to see all possibilities");
			e.addText(C.GREEN + "Costs " + F.f(cost) + " XP");
			w.addElement(e);
		}
		
		w.setViewport(3);
		w.setBackground(new PhantomElement(new ItemStack(Material.STAINED_GLASS_PANE), new Slot(0)).setMetadata((byte) 15));
		w.open();
	}
	
	public void openTierWindow(Player p, String tier)
	{
		int cost = (int) (costMultiple * this.cost.get(tier));
		
		if(SetExpFix.getTotalExperience(p) >= cost)
		{
			SetExpFix.setTotalExperience(p, SetExpFix.getTotalExperience(p) - cost);
			Enchant e = tierSet.get(tier).copy().pickRandom();
			CustomEnchantment en = (CustomEnchantment) e.getE();
			ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
			en.addToItem(is, e.getLevel());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(C.LIGHT_PURPLE + name + " " + M.toRoman(e.getLevel()));
			p.getInventory().addItem(is);
		}
		
		else
		{
			p.sendMessage(C.RED + "You only have " + F.f(SetExpFix.getTotalExperience(p)) + " XP");
		}
	}
	
	public void openRevealWindow(Player p, String tier)
	{
		Window w = new PhantomWindow(C.LIGHT_PURPLE + StringUtils.capitalize(tier) + " Possibilities", p);
		int ix = 0;
		
		for(Enchant i : tierSet.get(tier))
		{
			Element e = new PhantomElement(new ItemStack(Material.ENCHANTED_BOOK), new Slot(ix));
			e.setTitle(C.LIGHT_PURPLE + i.getE().getClass().getSimpleName().replaceAll("Enchantment", "") + " " + M.toRoman(i.getLevel()));
			e.addText(C.DARK_PURPLE + ((CustomEnchantment) i.getE()).getDescription());
			e.addText(C.RED + "Chance: " + F.pc(1.0 / (double) tierSet.get(tier).size()));
			w.addElement(e);
			ix++;
		}
		
		w.setBackground(new PhantomElement(new ItemStack(Material.STAINED_GLASS_PANE), new Slot(0)).setMetadata((byte) 15));
		w.open();
	}
	
	public void openSuperRevealWindow(Player p, String tier)
	{
		Window w = new PhantomWindow(C.LIGHT_PURPLE + StringUtils.capitalize(tier) + " Possibilities" + C.YELLOW + " " + ranged.get(tier).getMin() + " - " + ranged.get(tier).getMax(), p);
		int ix = 0;
		
		for(Enchant i : tierSet.get(tier))
		{
			Element e = new PhantomElement(new ItemStack(Material.ENCHANTED_BOOK), new Slot(ix));
			e.setTitle(C.LIGHT_PURPLE + i.getE().getClass().getSimpleName().replaceAll("Enchantment", "") + " " + M.toRoman(i.getLevel()));
			e.addText(C.DARK_PURPLE + ((CustomEnchantment) i.getE()).getDescription());
			e.addText(C.RED + "Chance: " + F.pc(1.0 / (double) tierSet.get(tier).size()));
			e.addText(C.YELLOW + "Member: " + i.getClass().getSimpleName());
			e.addText(C.YELLOW + "Intensity: " + i.getE().getIntensity(1) * i.getLevel());
			e.addText(C.YELLOW + "Max: " + ((CustomEnchantment) i.getE()).getMaxLevel());
			w.addElement(e);
			ix++;
		}
		
		w.setBackground(new PhantomElement(new ItemStack(Material.STAINED_GLASS_PANE), new Slot(0)).setMetadata((byte) 15));
		w.open();
	}
	
	public double getCost(Enchanted e, int level)
	{
		return costMultiple * (e.getIntensity(1) * (double) level);
	}
	
	public GList<Enchanted> getEnchantments()
	{
		GList<Enchanted> enchanted = new GList<Enchanted>();
		
		for(CustomEnchantment i : enchantments)
		{
			enchanted.add((Enchanted) i);
		}
		
		return enchanted;
	}
	
	public void onReadConfig()
	{
		for(CustomEnchantment i : enchantments)
		{
			Enchanted e = (Enchanted) i;
			e.setPow(getConfiguration().getDouble("enchantment." + i.getClass().getSimpleName().toLowerCase().replaceAll("enchantment", "")));
		}
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	public void onTick()
	{
		for(Player i : affected.k())
		{
			affected.put(i, (float) (affected.get(i) / (1.145f)));
			NMSX.showWeather(i, affected.get(i) > 6f ? 6f : affected.get(i));
			affected.put(i, affected.get(i) > 6f ? 6f : affected.get(i));
			
			if(affected.get(i) < 0.1)
			{
				affected.remove(i);
			}
		}
		
		for(Player i : Phantom.instance().onlinePlayers())
		{
			if(i.getInventory().getHelmet() != null && EnchantmentAPI.itemHasEnchantment(i.getInventory().getHelmet(), "Nocturnal"))
			{
				PE.NIGHT_VISION.a(1).d(10).c(i);
			}
			
			if(i.getInventory().getBoots() != null && EnchantmentAPI.itemHasEnchantment(i.getInventory().getBoots(), "Spring"))
			{
				PE.JUMP.a(1).d(10).c(i);
			}
			
			if(Math.random() < 0.687)
			{
				ItemStack is = i.getItemInHand();
				
				if(is != null)
				{
					for(CustomEnchantment j : EnchantmentAPI.getEnchantments(is).keySet())
					{
						if(j instanceof AmbientEffect)
						{
							if(Math.random() > 0.963)
							{
								((AmbientEffect) j).ambientPlay(i);
							}
						}
					}
				}
			}
		}
		
		if(Math.random() < 0.687)
		{
			if(Math.random() > 0.33)
			{
				for(Location i : enchanted.k())
				{
					if(!i.getBlock().getType().equals(Material.TNT))
					{
						enchanted.remove(i);
						
						continue;
					}
					
					for(String j : enchanted.get(i).k())
					{
						if(Math.random() > 0.46)
						{
							if(j.equalsIgnoreCase("Sticky"))
							{
								ParticleEffect.SLIME.display(1f, 2, i.clone().add(0.5, 1, 0.5), 12);
							}
							
							if(j.equalsIgnoreCase("Inferno"))
							{
								ParticleEffect.LAVA.display(1f, 1, i.clone().add(0.5, 1, 0.5), 12);
							}
							
							if(j.equalsIgnoreCase("Blast"))
							{
								ParticleEffect.ENCHANTMENT_TABLE.display(0.9f, 6, i.clone().add(0.5, 1, 0.5), 12);
							}
							
							if(j.equalsIgnoreCase("Defusing"))
							{
								ParticleEffect.CRIT_MAGIC.display(0.6f, 2, i.clone().add(0.5, 1, 0.5), 12);
							}
						}
					}
				}
			}
		}
	}
	
	public void affect(Player p)
	{
		affect(p, (float) (Math.random() * 0.4f));
	}
	
	public void affect(Player p, Float f)
	{
		if(!affected.containsKey(p))
		{
			affected.put(p, f);
		}
		
		else
		{
			affected.put(p, affected.get(p) + f);
		}
	}
	
	@PlayerOnly
	@Permission("effex.god")
	@Command("enchantall")
	public void onCommand(PhantomSender sender, PhantomCommand cmd)
	{
		for(CustomEnchantment i : EnchantmentAPI.getAllValidEnchants(sender.getPlayer().getItemInHand()))
		{
			if(i instanceof Enchanted)
			{
				i.addToItem(sender.getPlayer().getItemInHand(), i.getMaxLevel());
				sender.sendMessage(C.GREEN + "+ " + i.getClass().getSimpleName().replaceAll("Enchantment", "") + " " + M.toRoman(i.getMaxLevel()));
			}
		}
	}
	
	@EventHandler
	public void on(ProjectileLaunchEvent e)
	{
		if(e.getEntity().getType().equals(EntityType.ARROW))
		{
			Arrow a = (Arrow) e.getEntity();
			
			if(a.getShooter() instanceof Player && a.isCritical())
			{
				Player p = (Player) a.getShooter();
				ItemStack is = p.getItemInHand();
				
				if(EnchantmentAPI.itemHasEnchantment(is, "Ender") && M.r(0.28))
				{
					e.setCancelled(true);
					ControllerMessage msg = new ControllerMessage(Phantom.instance().getController("CommuneController"));
					msg.set("reset-pearl", p.getName());
					sendMessage("CommuneController", msg);
					p.launchProjectile(EnderPearl.class);
				}
				
				else if(EnchantmentAPI.itemHasEnchantment(is, "Heat Seeker") && M.r(0.38))
				{
					int level = EnchantmentAPI.getEnchantments(is).get(EnchantmentAPI.getEnchantment("Heat Seeker"));
					
					new TaskLater()
					{
						@Override
						public void run()
						{
							new Task(5)
							{
								@Override
								public void run()
								{
									if(a == null || a.isDead() || a.isOnGround())
									{
										cancel();
									}
									
									Area ar = new Area(a.getLocation(), 4 * level);
									Double least = Double.MAX_VALUE;
									LivingEntity target = null;
									
									for(Entity i : ar.getNearbyEntities())
									{
										Double distance = i.getLocation().distanceSquared(a.getLocation());
										
										boolean cold = false;
										
										if(i instanceof Player)
										{
											Player tt = (Player) i;
											for(ItemStack ix : tt.getInventory().getArmorContents())
											{
												if(EnchantmentAPI.itemHasEnchantment(ix, "Cold Blooded"))
												{
													cold = true;
													break;
												}
											}
										}
										
										if(distance < least && !i.equals(p) && i instanceof LivingEntity && !cold)
										{
											least = distance;
											target = (LivingEntity) i;
										}
									}
									
									if(target != null)
									{
										new LineParticleManipulator()
										{
											@Override
											public void play(Location arg0)
											{
												ParticleEffect.FIREWORKS_SPARK.display(0f, 1, arg0, 111);
											}
										}.play(target.getEyeLocation(), a.getLocation(), 11.1);
										a.teleport(target.getEyeLocation());
									}
								}
							};
						}
					};
				}
			}
		}
	}
	
	@EventHandler
	public void on(PlayerMoveBlockEvent e)
	{
		Player p = e.getPlayer();
		
		for(ItemStack i : p.getInventory().getArmorContents())
		{
			if(EnchantmentAPI.itemHasEnchantment(i, "Stamina"))
			{
				e.getPlayer().setSaturation(100f);
			}
		}
		
		for(ItemStack i : p.getInventory().getArmorContents())
		{
			if(EnchantmentAPI.itemHasEnchantment(i, "Moss"))
			{
				int level = EnchantmentAPI.getEnchantments(i).get(EnchantmentAPI.getEnchantment("Moss"));
				
				if(M.r(((double) level / 15.0)))
				{
					i.setDurability((short) (i.getDurability() - 1 < 0 ? 0 : i.getDurability() - 1));
				}
				
			}
		}
		
		if(EnchantmentAPI.itemHasEnchantment(p.getItemInHand(), "Moss"))
		{
			int level = EnchantmentAPI.getEnchantments(p.getItemInHand()).get(EnchantmentAPI.getEnchantment("Moss"));
			
			if(M.r(((double) level / 15.0)))
			{
				p.getItemInHand().setDurability((short) (p.getItemInHand().getDurability() - 1 < 0 ? 0 : p.getItemInHand().getDurability() - 1));
			}
		}
	}
	
	@EventHandler
	public void on(TNTPrimeEvent e)
	{
		if(enchanted.containsKey(e.getTntBlock().getLocation()))
		{
			if(enchanted.get(e.getTntBlock().getLocation()).containsKey("Sticky"))
			{
				new Task(0)
				{
					@Override
					public void run()
					{
						if(e.getTntEntity().isDead())
						{
							cancel();
							return;
						}
						
						e.getTntEntity().setVelocity(new Vector(0, 0, 0));
						e.getTntEntity().setFallDistance(0f);
						e.getTntEntity().teleport(e.getTntBlock().getLocation());
					}
				};
			}
			
			if(enchanted.get(e.getTntBlock().getLocation()).containsKey("Inferno"))
			{
				e.getTntEntity().setFireTicks(200);
				e.getTntEntity().setIsIncendiary(true);
			}
			
			if(enchanted.get(e.getTntBlock().getLocation()).containsKey("Blast"))
			{
				e.getTntEntity().setYield((enchanted.get(e.getTntBlock().getLocation()).get("Blast")) + 3f);
			}
			
			if(enchanted.get(e.getTntBlock().getLocation()).containsKey("Defusing"))
			{
				e.getTntEntity().setFuseTicks((e.getTntEntity().getFuseTicks() / enchanted.get(e.getTntBlock().getLocation()).get("Defusing")));
			}
			
			enchanted.remove(e.getTntBlock().getLocation());
		}
	}
	
	@EventHandler
	public void on(TNTDispenseEvent e)
	{
		Map<CustomEnchantment, Integer> mapped = EnchantmentAPI.getEnchantments(e.getItem());
		
		if(!mapped.isEmpty())
		{
			if(mapped.containsKey(EnchantmentAPI.getEnchantment("Sticky")))
			{
				new Task(0)
				{
					@Override
					public void run()
					{
						if(e.getTNT().isDead())
						{
							cancel();
							return;
						}
						
						e.getTNT().teleport(e.getTNT().getLocation().add(VectorMath.reverse(e.getTNT().getVelocity())));
						e.getTNT().setVelocity(new Vector(0, 0, 0));
						e.getTNT().setFallDistance(0f);
					}
				};
			}
			
			if(mapped.containsKey(EnchantmentAPI.getEnchantment("Inferno")))
			{
				e.getTNT().setFireTicks(200);
				e.getTNT().setIsIncendiary(true);
			}
			
			if(mapped.containsKey(EnchantmentAPI.getEnchantment("Blast")))
			{
				e.getTNT().setYield((mapped.get(EnchantmentAPI.getEnchantment("Blast"))) + 3f);
			}
			
			if(mapped.containsKey(EnchantmentAPI.getEnchantment("Defusing")))
			{
				e.getTNT().setFuseTicks((e.getTNT().getFuseTicks() / mapped.get(EnchantmentAPI.getEnchantment("Defusing"))));
			}
		}
	}
	
	@EventHandler
	public void on(BlockPlaceEvent e)
	{
		if(e.getBlock().getType().equals(Material.TNT))
		{
			GMap<String, Integer> mapped = new GMap<String, Integer>();
			
			if(EnchantmentAPI.itemHasEnchantment(e.getItemInHand(), "Sticky"))
			{
				int level = EnchantmentAPI.getEnchantments(e.getItemInHand()).get(EnchantmentAPI.getEnchantment("Sticky"));
				mapped.put("Sticky", level);
			}
			
			if(EnchantmentAPI.itemHasEnchantment(e.getItemInHand(), "Inferno"))
			{
				int level = EnchantmentAPI.getEnchantments(e.getItemInHand()).get(EnchantmentAPI.getEnchantment("Inferno"));
				mapped.put("Inferno", level);
			}
			
			if(EnchantmentAPI.itemHasEnchantment(e.getItemInHand(), "Blast"))
			{
				int level = EnchantmentAPI.getEnchantments(e.getItemInHand()).get(EnchantmentAPI.getEnchantment("Blast"));
				mapped.put("Blast", level);
			}
			
			if(EnchantmentAPI.itemHasEnchantment(e.getItemInHand(), "Defusing"))
			{
				int level = EnchantmentAPI.getEnchantments(e.getItemInHand()).get(EnchantmentAPI.getEnchantment("Defusing"));
				mapped.put("Defusing", level);
			}
			
			if(!mapped.isEmpty())
			{
				enchanted.put(e.getBlock().getLocation(), mapped);
			}
		}
	}
	
	@EventHandler
	public void on(EntityDamageEvent e)
	{
		if(e.getCause().equals(DamageCause.FALL))
		{
			if(e.getEntity().getType().equals(EntityType.PLAYER))
			{
				Player p = (Player) e.getEntity();
				
				for(ItemStack i : p.getInventory().getArmorContents())
				{
					if(EnchantmentAPI.itemHasEnchantment(i, "Anti Gravity"))
					{
						e.setCancelled(true);
						e.setDamage(0);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void on(PlayerItemConsumeEvent e)
	{
		if(e.getItem().getType().equals(Material.POTION))
		{
			Player p = e.getPlayer();
			
			for(ItemStack i : p.getInventory().getArmorContents())
			{
				if(EnchantmentAPI.itemHasEnchantment(i, "Alchemist"))
				{
					int level = EnchantmentAPI.getEnchantments(i).get(EnchantmentAPI.getEnchantment("Alchemist"));
					int bottles = W.count(e.getPlayer(), new MaterialBlock(Material.GLASS_BOTTLE));
					
					if(M.r(0.38 + ((double) level / 15.0)))
					{
						ItemStack is = e.getItem().clone();
						p.sendMessage("** Potion Saved **");
						
						new TaskLater()
						{
							@Override
							public void run()
							{
								p.setItemInHand(is);
								
								if(W.count(e.getPlayer(), new MaterialBlock(Material.GLASS_BOTTLE)) > bottles)
								{
									W.take(e.getPlayer(), new MaterialBlock(Material.GLASS_BOTTLE), W.count(e.getPlayer(), new MaterialBlock(Material.GLASS_BOTTLE)) - bottles);
								}
							}
						};
					}
				}
			}
		}
	}
	
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		if(e.getBlock().getType().equals(Material.TNT))
		{
			if(enchanted.containsKey(e.getBlock().getLocation()))
			{
				e.getBlock().setType(Material.AIR);
				e.setCancelled(true);
				ItemStack ntnt = new ItemStack(Material.TNT);
				
				for(String i : enchanted.get(e.getBlock().getLocation()).k())
				{
					int level = enchanted.get(e.getBlock().getLocation()).get(i);
					
					EnchantmentAPI.getEnchantment(i).addToItem(ntnt, level);
				}
				
				if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
				{
					e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), ntnt);
				}
				
				enchanted.remove(e.getBlock().getLocation());
			}
		}
		
		if(e.getPlayer().getInventory().getBoots() != null && EnchantmentAPI.itemHasEnchantment(e.getPlayer().getInventory().getBoots(), "Magnet"))
		{
			if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			{
				Collection<ItemStack> is = e.getBlock().getDrops(e.getPlayer().getItemInHand());
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				
				new TaskLater()
				{
					@Override
					public void run()
					{
						for(ItemStack i : is)
						{
							if(e.getPlayer().getItemInHand() != null && EnchantmentAPI.itemHasEnchantment(e.getPlayer().getItemInHand(), "Forge"))
							{
								boolean part = false;
								
								if(e.getBlock().getType().equals(Material.IRON_ORE))
								{
									part = true;
									
									if(new PhantomInventory(e.getPlayer().getInventory()).hasSpace())
									{
										e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT));
									}
									
									else
									{
										e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
									}
								}
								
								if(e.getBlock().getType().equals(Material.GOLD_ORE))
								{
									part = true;
									
									if(new PhantomInventory(e.getPlayer().getInventory()).hasSpace())
									{
										e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT));
									}
									
									else
									{
										e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
									}
								}
								
								if(part)
								{
									ParticleEffect.LAVA.display(0.2f, 4, e.getBlock().getLocation(), 24);
									new GSound(Sound.FIZZ, 0.4f, 1f).play(e.getPlayer().getLocation());
								}
							}
							
							else
							{
								
							}
							
							if(new PhantomInventory(e.getPlayer().getInventory()).hasSpace())
							{
								e.getPlayer().getInventory().addItem(i);
							}
							
							else
							{
								e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), i);
							}
						}
					}
				};
			}
		}
		
		if(e.getPlayer().getItemInHand() != null && EnchantmentAPI.itemHasEnchantment(e.getPlayer().getItemInHand(), "Forge"))
		{
			boolean part = false;
			
			if(e.getBlock().getType().equals(Material.IRON_ORE))
			{
				part = true;
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));
			}
			
			if(e.getBlock().getType().equals(Material.GOLD_ORE))
			{
				part = true;
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
			}
			
			if(part)
			{
				ParticleEffect.LAVA.display(0.2f, 4, e.getBlock().getLocation(), 24);
				new GSound(Sound.FIZZ, 0.4f, 1f).play(e.getPlayer().getLocation());
				e.getPlayer().getItemInHand().setDurability((short) (e.getPlayer().getItemInHand().getDurability() + 3));
			}
		}
//TODO sdfsdfsdfsdf
//		if(e.getPlayer().getItemInHand() != null && EnchantmentAPI.itemHasEnchantment(e.getPlayer().getItemInHand(), "Blast"))
//		{
//			if(ignored.contains(e.getPlayer()))
//			{
//				return;
//			}
//			
//			int level = EnchantmentAPI.getEnchantments(e.getPlayer().getItemInHand()).get(EnchantmentAPI.getEnchantment("Blast"));
//			GList<Block> breaks = new GList<Block>();
//			breaks.add(e.getBlock().getRelative(BlockFace.UP));
//			breaks.add(e.getBlock().getRelative(BlockFace.DOWN));
//			
//			if(level > 1)
//			{
//				breaks.add(e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
//				breaks.add(e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
//				breaks.add(e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
//				breaks.add(e.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
//				breaks.add(e.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH));
//				breaks.add(e.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH));
//				breaks.add(e.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST));
//				breaks.add(e.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH));
//				breaks.add(e.getBlock().getRelative(BlockFace.EAST));
//				breaks.add(e.getBlock().getRelative(BlockFace.WEST));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getRelative(BlockFace.UP));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getRelative(BlockFace.DOWN));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST));
//				breaks.add(e.getBlock().getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST));
//				breaks.add(e.getBlock().getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST));
//			}
//			
//			breaks.removeDuplicates();
//			breaks.pickRandom();
//			ignored.add(e.getPlayer());
//			e.getPlayer().getItemInHand().setDurability((short) (e.getPlayer().getItemInHand().getDurability() + breaks.size()));
//			
//			new Task(0)
//			{
//				@SuppressWarnings("deprecation")
//				@Override
//				public void run()
//				{
//					for(int k = 0; k < level; k++)
//					{
//						if(breaks.isEmpty())
//						{
//							cancel();
//							ignored.remove(e.getPlayer());
//							return;
//						}
//						
//						Block b = breaks.pop();
//						BlockBreakEvent bbe = new BlockBreakEvent(b, e.getPlayer());
//						callEvent(bbe);
//						
//						if(!bbe.isCancelled() && !b.getType().equals(Material.BEDROCK))
//						{
//							b.breakNaturally(e.getPlayer().getItemInHand());
//							b.getWorld().playEffect(b.getLocation().add(0.5, 0.5, 0.5), Effect.TILE_BREAK, b.getTypeId());
//						}
//					}
//				}
//			};
//		}
	}
	
	@EventHandler
	public void on(PlayerKillPlayerEvent e)
	{
		Player target = e.getPlayer();
		Player user = e.getDamager();
		ItemStack is = user.getItemInHand();
		
		if(is != null && EnchantmentAPI.itemHasEnchantment(is, "Beheading"))
		{
			int enchantLevel = EnchantmentAPI.getEnchantments(is).get(EnchantmentAPI.getEnchantment("Beheading"));
			
			if(Math.random() > 0.565 - (enchantLevel / 5))
			{
				Location a = P.getHand((Player) user);
				Vector dir = VectorMath.direction(a, target.getLocation().add(0, 1.5, 0));
				Location b = target.getEyeLocation();
				
				new VampiricEffect(1.6f * enchantLevel).play(a, dir, b.add(0, 1.5, 0));
				Location s = target.getLocation().clone();
				s.setY(0);
				new GSound(Sound.SLIME_ATTACK, 2.0f, 1.8f).play(target.getLocation());
				new GSound(Sound.SLIME_ATTACK, 2.0f, 1.0f).play(target.getLocation());
				new GSound(Sound.SLIME_ATTACK, 2.0f, 0.2f).play(target.getLocation());
				new GSound(Sound.SLIME_ATTACK, 2.0f, 0.5f).play(target.getLocation());
				
				String pl = target.getName();
				ItemStack skull = new ItemStack(Material.SKULL_ITEM);
				skull.setDurability((short) 3);
				SkullMeta sm = (SkullMeta) skull.getItemMeta();
				sm.setOwner(pl);
				sm.setDisplayName(ChatColor.DARK_RED + "" + pl);
				sm.setLore(new GList<String>().qadd(C.YELLOW + "Killed by " + user.getName()));
				skull.setItemMeta(sm);
				target.getLocation().getWorld().dropItem(target.getLocation(), skull);
			}
		}
		
	}
	
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Arrow)
		{
			if(e.getEntity() instanceof Player)
			{
				Player p = (Player) e.getEntity();
				
				if(p.isBlocking())
				{
					try
					{
						ItemStack is = p.getItemInHand();
						
						if(is != null)
						{
							if(EnchantmentAPI.getEnchantments(is).containsKey(EnchantmentAPI.getEnchantment("Deflection")))
							{
								int level = EnchantmentAPI.getEnchantments(is).get(EnchantmentAPI.getEnchantment("Deflection"));
								
								if(Math.random() > 0.999 - ((double) level / 4.0))
								{
									e.setCancelled(true);
									NMSX.breakParticles(e.getDamager().getLocation(), Material.WOOD, 8);
									NMSX.breakParticles(e.getDamager().getLocation(), Material.STONE, 8);
									new GSound(Sound.ITEM_BREAK, 1f, 1.7f).play(e.getEntity().getLocation());
									e.getDamager().remove();
								}
							}
						}
					}
					
					catch(Exception ex)
					{
						
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(InventoryClickEvent e)
	{
		ItemStack drop = e.getCursor();
		ItemStack targ = e.getCurrentItem();
		
		if(targ != null && drop != null)
		{
			if(drop.getType().equals(Material.ENCHANTED_BOOK) && (targ.getType().equals(Material.FISHING_ROD) || targ.getType().toString().contains("SWORD") || targ.getType().toString().contains("HOE") || targ.getType().toString().contains("SPADE") || targ.getType().toString().contains("AXE") || targ.getType().toString().contains("SHEAR") || targ.getType().toString().contains("BOOT") || targ.getType().toString().contains("LEGG") || targ.getType().toString().contains("CHESTPL") || targ.getType().toString().contains("HELMET") || targ.getType().toString().equals("TNT")))
			{
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) drop.getItemMeta();
				
				for(CustomEnchantment i : new GList<CustomEnchantment>(EnchantmentAPI.getEnchantments(drop).keySet()))
				{
					if(i.canEnchantOnto(targ))
					{
						i.removeFromItem(targ);
						i.addToItem(targ, EnchantmentAPI.getEnchantments(drop).get(i));
					}
					
					else
					{
						return;
					}
				}
				
				Stack t = new Stack(targ);
				
				for(Enchantment i : meta.getStoredEnchants().keySet())
				{
					try
					{
						if(!i.getItemTarget().includes(targ.getType()))
						{
							e.getWhoClicked().sendMessage(F.color("&8&l(&c&l!&8&l) &cInvalid &e&l" + i.getName().substring(0, 1) + i.getName().toLowerCase().replaceAll("_", " ").substring(1) + "&c -> &e&l" + targ.getType().toString().substring(0, 1) + targ.getType().toString().toLowerCase().replaceAll("_", " ").substring(1)));
							return;
						}
					}
					
					catch(Exception ex)
					{
						
					}
				}
				
				for(Enchantment i : meta.getStoredEnchants().keySet())
				{
					t.getEnchantmentSet().addEnchantment(i, meta.getStoredEnchants().get(i));
				}
				
				StackedInventory inv = new StackedInventory(e.getClickedInventory());
				inv.setStack(e.getSlot(), t);
				inv.thrash();
				
				e.setCursor(null);
				e.setCancelled(true);
			}
		}
		
		if(targ != null && drop != null)
		{
			if(drop.getType().equals(Material.BOOK) && (targ.getType().equals(Material.FISHING_ROD) || targ.getType().toString().contains("SWORD") || targ.getType().toString().contains("HOE") || targ.getType().toString().contains("SPADE") || targ.getType().toString().contains("AXE") || targ.getType().toString().contains("SHEAR") || targ.getType().toString().contains("BOOT") || targ.getType().toString().contains("LEGG") || targ.getType().toString().contains("CHESTPL") || targ.getType().toString().contains("HELMET") || targ.getType().toString().equals("TNT")))
			{
				for(CustomEnchantment i : new GList<CustomEnchantment>(EnchantmentAPI.getEnchantments(drop).keySet()))
				{
					if(i.canEnchantOnto(targ))
					{
						i.removeFromItem(targ);
						i.addToItem(targ, EnchantmentAPI.getEnchantments(drop).get(i));
					}
					
					else
					{
						return;
					}
				}
				
				e.setCursor(null);
				e.setCancelled(true);
			}
		}
	}
}