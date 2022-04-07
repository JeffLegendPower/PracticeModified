package me.trixxtraxx.Practice;

import me.trixxtraxx.Practice.GameEvents.GameEvent;
import me.trixxtraxx.Practice.GameLogic.Components.GameComponent;
import me.trixxtraxx.Practice.Kit.KitComponent;
import me.trixxtraxx.Practice.Map.MapComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class ComponentClass<E extends Component>
{
    protected List<E> components = new ArrayList<>();
    public List<E> getComponents()
    {
        return components;
    }
    public void addComponent(E comp)
    {
        components.add(comp);
    }
    public void removeComponent(E comp)
    {
        components.remove(comp);
    }
    
    public List<E> getComponents(Class<?> c)
    {
        List<E> comps = new ArrayList<>();
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
    
    //Trigger all functions that have the name "onEvent" in this class
    //then if the event implements cancellable, call the onEventCancel functions is it isCancled, otherwise call the onEventAfter functions
    public Event triggerEvent(Event e)
    {
        for(Component comp:components)
        {
            Class<?> c = comp.getClass();
            //get all methods that have the annotation @TriggerEvent, and call them in order of their priority from lowest to highest
            List<MethodData> methods = new ArrayList<>();
            for(Method m:c.getMethods())
            {
                if(m.isAnnotationPresent(TriggerEvent.class))
                {
                    //continue if more then 1 parameter is given or the parameter cant be cast from the event
                    if(m.getParameterCount() != 1 || !m.getParameterTypes()[0].isAssignableFrom(e.getClass())) continue;
                    TriggerEvent event = m.getAnnotation(TriggerEvent.class);
                    methods.add(new MethodData()
                    {
                        {
                            method = m;
                            event = event;
                            component = comp;
                        }
                    });
                }
            }
            methods.sort((a,b)->a.event.priority()-b.event.priority());
            for(MethodData method:methods)
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
            if(e instanceof Cancellable)
            {
                if(((Cancellable)e).isCancelled())
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
        }
        return e;
    }
    public GameEvent triggerEvent(GameEvent e)
    {
        for(Component comp:components)
        {
            Class<?> c = comp.getClass();
            //get all methods that have the annotation @TriggerEvent, and call them in order of their priority from lowest to highest
            List<MethodData> methods = new ArrayList<>();
            for(Method m:c.getMethods())
            {
                if(m.isAnnotationPresent(TriggerEvent.class))
                {
                    //continue if more then 1 parameter is given or the parameter cant be cast from the event
                    if(m.getParameterCount() != 1 || !m.getParameterTypes()[0].isAssignableFrom(e.getClass())) continue;
                    TriggerEvent event = m.getAnnotation(TriggerEvent.class);
                    methods.add(new MethodData()
                    {
                        {
                            method = m;
                            event = event;
                            component = comp;
                        }
                    });
                }
            }
            methods.sort(Comparator.comparingInt((MethodData a) -> a.event.priority()));
            
            for(MethodData method:methods)
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
            if(e instanceof Cancellable)
            {
                if(((Cancellable)e).isCancelled())
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
        }
        return e;
    }
    
    
    public String applyPlaceholders(Player p, String s)
    {
        for (Component comp : getComponents()) s = comp.applyPlaceholder(p, s);
        return s;
    }
    public List<String> applyPlaceholders(Player p, List<String> list)
    {
        List<String> newStrings = new ArrayList<>();
        for (String string:list)
        {
            String s = string;
            applyPlaceholders(p, s);
            newStrings.add(s);
        }
        return newStrings;
    }
}
