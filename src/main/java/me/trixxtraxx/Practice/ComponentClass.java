package me.trixxtraxx.Practice;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.GameLogic.SoloGameLogic.Events.DropEvent;
import me.trixxtraxx.Practice.Kit.KitComponent;
import me.trixxtraxx.Practice.Map.MapComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import me.TrixxTraxx.Linq.List;

public abstract class ComponentClass<E extends Component>
{
    protected List<E> components = new List<>();
    public List<E> getComponents()
    {
        return components;
    }
    public void setComponents(List<E> comps){components = comps;}
    public void addComponent(E comp){
        Practice.log(4, "Adding component " + comp.getClass().getSimpleName());
        components.add(comp);
    }
    public void removeComponent(E comp)
    {
        components.remove(comp);
    }
    
    public List<E> getComponents(Class<?> c)
    {
        List<E> comps = new List<>();
        for (E comp:components)
        {
            if(c.isInstance(comp)) comps.add(comp);
        }
        return comps;
    }
    
    private class MethodData
    {
        public Method method;
        public TriggerEvent event;
        public Component component;
    }
    
    public <T> T triggerEvent(T e)
    {
        if(e.getClass() == DropEvent.class) Practice.log(5, "Triggering " + getClass().getSimpleName() + "; checking " + getComponents().size() + " components");
        List<MethodData> methods = new List<>();
        for(Component comp: getComponents())
        {
            Class<?> c = comp.getClass();
            //get all methods that have the annotation @TriggerEvent, and call them in order of their priority from lowest to highest
            if(e.getClass() == DropEvent.class) Practice.log(5, "Checking Component: " + c.getSimpleName());
            for(Method m: c.getMethods())
            {
                TriggerEvent eventAnnotation = m.getAnnotation(TriggerEvent.class);
                if(eventAnnotation != null)
                {
                    //continue if more then 1 parameter is given or the parameter cant be cast from the event
                    if(m.getParameterCount() != 1 || !m.getParameterTypes()[0].isAssignableFrom(e.getClass())) continue;
                    if(e.getClass() == DropEvent.class) Practice.log(5, "Found drop event method: " + m.getName() + " in " + c.getSimpleName());
                    methods.add(new MethodData()
                    {
                        {
                            method = m;
                            event = eventAnnotation;
                            component = comp;
                        }
                    });
                }
            }
        }
        methods.sort((a, b) -> a.event.priority() - b.event.priority());
        for(MethodData method: methods)
        {
            if(method.event.state() == TriggerEvent.CancelState.NONE)
            {
                try
                {
                    //invoke but cast the event to the first parameter of the method
                    method.method.invoke(method.component, method.method.getParameterTypes()[0].cast(e));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        
        if(e instanceof Cancellable || e instanceof GameEvent)
        {
            if((e instanceof Cancellable && ((Cancellable) e).isCancelled()) || (e instanceof GameEvent && ((GameEvent) e).isCanceled()))
            {
                for(MethodData method: methods)
                {
                    if(method.event.state() == TriggerEvent.CancelState.ENSURE_CANCEL)
                    {
                        try
                        {
                            //invoke but cast the event to the first parameter of the method
                            method.method.invoke(method.component, method.method.getParameterTypes()[0].cast(e));
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
                return e;
            }
        }
    
        for(MethodData method: methods)
        {
            if(method.event.state() == TriggerEvent.CancelState.ENSURE_NOT_CANCEL)
            {
                try
                {
                    //invoke but cast the event to the first parameter of the method
                    method.method.invoke(method.component, method.method.getParameterTypes()[0].cast(e));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return e;
    }
    
    public String applyPlaceholders(Player p, String s)
    {
        if(s == null) return s;
        for (Component comp : getComponents())
        {
            try
            {
                s = comp.applyPlaceholder(p, s);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return s;
    }
    public List<String> applyPlaceholders(Player p, List<String> list)
    {
        List<String> newStrings = new List<>();
        for (String string:list)
        {
            newStrings.add(applyPlaceholders(p, string));
        }
        return newStrings;
    }
}
