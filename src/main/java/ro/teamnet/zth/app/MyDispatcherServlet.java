package ro.teamnet.zth.app;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.api.annotations.MyRequestParam;
import ro.teamnet.zth.app.controller.DepartmentController;
import ro.teamnet.zth.app.controller.EmployeeController;
import ro.teamnet.zth.app.controller.JobsController;
import ro.teamnet.zth.app.controller.LocationController;
import ro.teamnet.zth.fmk.AnnotationScanUtils;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Mi on 5/6/2015.
 */
public class MyDispatcherServlet extends HttpServlet {

    HashMap<String, MethodAttributes> allowedMethods = new HashMap<>();
    @Override
    public void init() throws ServletException {
        Iterable<Class> classes;
        try {
            classes = AnnotationScanUtils.getClasses("ro.teamnet.zth.app.controller");
            getAllowedMethods(classes);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAllowedMethods(Iterable<Class> classes) {

        for (Class controller : classes) {
            if (controller.isAnnotationPresent(MyController.class)) {
                MyController controlAnnotation = (MyController)controller.getAnnotation(MyController.class);
                String controllerUrlPath =  controlAnnotation.urlPath();
                Method[] methods = controller.getMethods();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(MyRequestMethod.class)) {
                        MyRequestMethod methodAnnotation = (MyRequestMethod) m.getAnnotation(MyRequestMethod.class);
                        String key = controllerUrlPath + methodAnnotation.urlPath();

                        MethodAttributes methodAttributes = new MethodAttributes();
                        methodAttributes.setControllerClass(controller.getName());
                        methodAttributes.setMethodName(m.getName());
                        methodAttributes.setMethodType(methodAnnotation.methodType());
                        methodAttributes.setMethodParameterTypes(m.getParameterTypes());
                        allowedMethods.put(key, methodAttributes);
                    }
                }

            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* Delegate task to application controller */
        dispatchReply("GET", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* Delegate task to application controller */
        dispatchReply("POST", req, resp);
    }

    private void dispatchReply(String method, HttpServletRequest req, HttpServletResponse resp) {

        Object r = null;
        try {
            r = dispatch(req, resp);
            reply(r, req, resp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DispatchException dException){
            try {
                sendException(dException, resp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void sendException(DispatchException dException, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("Nu s-a mapat URL-ul");
    }

    private Object dispatch(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String path = req.getPathInfo();
        MethodAttributes mAtributes = allowedMethods.get(path);
        if (mAtributes != null) {
            //iau clasa si instantiez obiectul controller
            Class<?> controller = Class.forName(mAtributes.getControllerClass());
            Object obj  = controller.newInstance();
            Method method = controller.getMethod(mAtributes.getMethodName(), mAtributes.getMethodParameterTypes());

            //adnotari parametri metode
            Annotation[][] paramAnnotation = method.getParameterAnnotations();
            if (paramAnnotation.length > 0) {
                MyRequestParam annotation = (MyRequestParam)paramAnnotation[0][0];
                //valorile parametrilor
                List<String> methodParamsValues = new ArrayList<>();
                String pName = annotation.paramName();
                methodParamsValues.add(req.getParameter(pName));

                return method.invoke(obj, methodParamsValues.toArray(new String[0]));
            } else
                return method.invoke(obj);
        }
        throw new DispatchException();
    }

    private void reply(Object r, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(r);
        resp.getWriter().write(json);
    }

}
