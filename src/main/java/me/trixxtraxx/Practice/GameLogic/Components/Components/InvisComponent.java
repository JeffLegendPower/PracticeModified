package me.trixxtraxx.Practice.GameLogic.Components.Components;

import me.TrixxTraxx.Linq.List;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.GameLogic;
import me.trixxtraxx.Practice.Practice;
import me.trixxtraxx.Practice.TriggerEvent;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Random;

public class InvisComponent extends GameComponent
{
    public InvisComponent(GameLogic logic)
    {
        super(logic);
    }
    
    private static List<InvisiblePlayer> Invisible = new List<>();
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void OnInvis(PlayerItemConsumeEvent e)
    {
        Practice.log(4, "Invis Comp event called");
        if(e.getItem().getType() == Material.POTION)
        {
            Player p = e.getPlayer();
            int dur = getDuration(e.getItem());
            Practice.log(4, "Duration:" + dur);
            if( dur == -1) return;
            Practice.log(4, "Removing Armor!");
            InvisiblePlayer invis = getInvisPlayer(p);
            if(invis == null)
            {
                invis = new InvisiblePlayer(p);
                Invisible.add(invis);
            }
            invis.SetTimer(dur);
            invis.removeArmor();
        }
    }
    
    @TriggerEvent(state = TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
    public void OnEntityDamage(EntityDamageByEntityEvent e)
    {
        Practice.log(4, "Invis Comp event called2");
        if(!e.isCancelled())
        {
            if (e.getEntity() instanceof Player)
            {
                Player p = (Player) e.getEntity();
                InvisiblePlayer inv = getInvisPlayer(p);
                if (inv != null)
                {
                    inv.addArmor();
                    removeInvis(p);
                    e.setDamage(e.getDamage() / 2);
                }
            }
        }
    }
    
    public static void removeInvis(Player p)
    {
        InvisiblePlayer inv = getInvisPlayer(p);
        if(inv != null) Invisible.remove(inv);
    }
    
    private static int getDuration(ItemStack stack)
    {
        if(!stack.hasItemMeta()) return -1;
        if(stack.getItemMeta() instanceof  PotionMeta)
        {
            for (PotionEffect e : ((PotionMeta) stack.getItemMeta()).getCustomEffects())
            {
                Practice.log(4, "EffectType: " + e.getType().getName() + ", Duration: " + e.getDuration());
                if (e.getType().getName().equalsIgnoreCase("INVISIBILITY")) return e.getDuration();
            }
        }
        return -1;
    }
    
    private static InvisiblePlayer getInvisPlayer(Player p)
    {
        for (InvisiblePlayer inv : Invisible)
        {
            if(inv.p == p) return inv;
        }
        return null;
    }
    
    public class InvisiblePlayer
    {
        public Player p;
        private BukkitRunnable run;
        private BukkitRunnable run2;
        private Team st;
        private boolean isDestroyed;
        private ItemStack[] armor;
        
        
        public InvisiblePlayer(Player p)
        {
            this.p = p;
            isDestroyed = false;
        }
        
        private String token()
        {
            char[] values =
                    {
                            'q','w','e','r','t','z','u','i','o','p','a','s','d','f','g','h','j','k','l','y','x','c','v','b','n','m'
                    };
            String generatedString ="";
            Random rdm = new Random();
            for (int i = 0; i < 10; i++)
            {
                generatedString += values[rdm.nextInt(values.length)];
            }
            
            return generatedString;
        }
        
        public void removeArmor()
        {
            hideArmor();
        }
        
        public void SetTimer(int ticks)
        {
            if(run != null) run.cancel();
            run = new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    addArmor();
                    Destroy();
                }
            };
            run.runTaskLater(Practice.Instance, ticks);
        }
        
        public void addArmor()
        {
            showArmor();
        }
        
        public void Destroy()
        {
            if(!isDestroyed)
            {
                isDestroyed = true;
                removeInvis(p);
                if (run != null) run.cancel();
                run = null;
                if (run2 != null) run2.cancel();
                run2 = null;
                p = null;
            }
        }
        
        private void hideArmor()
        {
            run2 = new Step(p);
            run2.runTaskTimer(Practice.Instance, 10, 20);
            Practice.log(4, "NOW HIDING ARMOR");
            
            Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
            st = sb.registerNewTeam(token());
            st.setNameTagVisibility(NameTagVisibility.NEVER);
            hide();
        }
        
        private void hide()
        {
            if(isDestroyed || logic.getGame().hasEnded()) return;
            
            try
            {
                hide2();
            }
            catch (Exception ex)
            {
                Practice.log(1, "CANCELING INVIS LISTENER");
                ex.printStackTrace();
            }
        }
        
        private void hide2()
        {
            armor = p.getInventory().getArmorContents();
            p.getInventory().setArmorContents(null);
        }
        
        private void showArmor()
        {
            st.unregister();
            Practice.log(4, "NOW SHOWING");
            p.getInventory().setArmorContents(armor);
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            
            Destroy();
        }
    }
    
    public class Step extends BukkitRunnable
    {
        private boolean foot;
        private Player p;
        
        public Step(Player p)
        {
            this.p = p;
        }
        
        @Override
        public void run()
        {
            Location l = p.getLocation(); //Get the player's location
            l.setY(Math.floor(l.getY())); //Make sure the location's y is an integer
            
            if (!l.clone().subtract(0, 1, 0).getBlock().isEmpty()) //Get the block under the player's feet and make sure it exists (This prevents footprints from spawning in the air)
            {
                double x = Math.cos(Math.toRadians(p.getLocation().getYaw())) * 0.25d; //If you don't understand trigonometry, just think of it as rotating the footprints to the direction the player is looking.
                double y = Math.sin(Math.toRadians(p.getLocation().getYaw())) * 0.25d;
                
                if (foot) //This code just modifies the location with the rotation and the current foot
                    l.add(x, 0.025D, y);
                else
                    l.subtract(x, -0.025D, y);
    
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FOOTSTEP, true, (float) l.getX(), (float) l.getY(), (float) l.getZ(), 0, 0, 0, 0, 1); //Create the packet
                for (Player receiver : logic.getPlayers()) //Send the packet to all players
                    ((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(packet);
            }
            
            
            foot = !foot;
        }
        
    }
}
