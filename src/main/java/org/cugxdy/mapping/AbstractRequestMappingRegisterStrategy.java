package org.cugxdy.mapping;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.cugxdy.annotation.JsonResponse;
import org.cugxdy.annotation.PathVariable;
import org.cugxdy.annotation.RequestBody;
import org.cugxdy.annotation.RequestHeader;
import org.cugxdy.annotation.RequestParam;
import org.cugxdy.annotation.UploadFile;
import org.cugxdy.annotation.UploadFiles;
import org.cugxdy.annotation.UrlEncodedForm;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import io.netty.handler.codec.http.FullHttpRequest;

// 模板设计模式
public abstract class AbstractRequestMappingRegisterStrategy implements RequestMappingRegisterStrategy {

	@Override
	public void register(Class<?> clazz, String baseUrl, Method method) {
		// TODO Auto-generated method stub
		// 获取方法上的Url
		String methodMappingUrl = getMethodUrl(method);
		
		String url = getMethodUrl(baseUrl, methodMappingUrl);
		if(url == null || url.trim().isEmpty()) {
			return ;
		}
		System.out.println(url);
		
		// ControllerMapping对应于方法级别的映射关系。
		ControllerMapping controllerMapping = new ControllerMapping();
		controllerMapping.setUrl(url);
		controllerMapping.setClassMethod(method.getName());
		controllerMapping.setClassName(clazz.getName());
		System.out.println("JsonResponse = " + (method.getAnnotation(JsonResponse.class) != null));
		controllerMapping.setJsonResponse(method.getAnnotation(JsonResponse.class) != null);
		
		String httpMethod = getHttpMethod();
		
		if(httpMethod != null) {
			Parameter[] param = method.getParameters();
			
			if(param.length > 0) {
				String[] paramNames = getMethodParameterNamesByAsm4(clazz, method);
				for(int i=0;i < param.length ; i++) {
					
					ControllerMappingParameter cmp = new ControllerMappingParameter();
					cmp.setDataType(param[i].getType());
					
					System.out.println(paramNames[i]);
					
					// 判断是否FullHttpRequest类型参数
					if(param[i].getType().equals(FullHttpRequest.class)) {
                        cmp.setName(paramNames[i]);
                        cmp.setType(ControllerMappingParameterTypeEnum.HTTP_REQUEST);
                        controllerMapping.getParameters().add(cmp);
                        continue;
					}
					
					// 判断是否HTTP_RESPONSE类型参数
	                if(param[i].getType().equals(org.cugxdy.rest.HttpResponse.class)) {
	                    cmp.setName(paramNames[i]);
	                    cmp.setType(ControllerMappingParameterTypeEnum.HTTP_RESPONSE);
	                    controllerMapping.getParameters().add(cmp);
	                    continue;
	                }  
	                
	                // 判断是否需要参数
                    if(param[i].getAnnotation(RequestParam.class) != null) {
                        org.cugxdy.annotation.RequestParam requestParam = param[i].getAnnotation(org.cugxdy.annotation.RequestParam.class);
                        cmp.setName(requestParam.value());
                        cmp.setRequired(requestParam.required());
                        cmp.setType(ControllerMappingParameterTypeEnum.REQUEST_PARAM);
                        controllerMapping.getParameters().add(cmp);
                        continue;
                    }
                    
                    // 判断是否需要请求头数据
                    if(param[i].getAnnotation(RequestHeader.class) != null) {
                        RequestHeader requestHeader = param[i].getAnnotation(RequestHeader.class);
                        cmp.setName((requestHeader.value() != null && !requestHeader.value().trim().isEmpty()) ?
                                requestHeader.value().trim() : paramNames[i]);
                        cmp.setRequired(requestHeader.required());
                        cmp.setType(ControllerMappingParameterTypeEnum.REQUEST_HEADER);
                        controllerMapping.getParameters().add(cmp);
                        continue;
                    }
                    
                    if(param[i].getAnnotation(PathVariable.class) != null) {
                        PathVariable pathVariable = param[i].getAnnotation(PathVariable.class);
                        cmp.setName((pathVariable.value() != null && !pathVariable.value().trim().isEmpty()) ?
                                pathVariable.value().trim() : paramNames[i]);
                        cmp.setType(ControllerMappingParameterTypeEnum.PATH_VARIABLE);
                        controllerMapping.getParameters().add(cmp);
                        continue;
                    }
                    
                    if(param[i].getAnnotation(RequestBody.class) != null) {
                        cmp.setName(paramNames[i]);
                        cmp.setType(ControllerMappingParameterTypeEnum.REQUEST_BODY);
                        controllerMapping.getParameters().add(cmp);
                        continue;
                    }
                    
                    if(param[i].getAnnotation(UrlEncodedForm.class) != null) {
                        cmp.setName(paramNames[i]);
                        cmp.setType(ControllerMappingParameterTypeEnum.URL_ENCODED_FORM);
                        controllerMapping.getParameters().add(cmp);
                        continue;
                    }
                    
                    if(param[i].getAnnotation(UploadFile.class) != null) {
                        cmp.setName(paramNames[i]);
                        cmp.setType(ControllerMappingParameterTypeEnum.UPLOAD_FILE);
                        controllerMapping.getParameters().add(cmp);
                        continue;
                    }
                    
                    if(param[i].getAnnotation(UploadFiles.class) != null) {
                        cmp.setName(paramNames[i]);
                        cmp.setType(ControllerMappingParameterTypeEnum.UPLOAD_FILES);
                        controllerMapping.getParameters().add(cmp);
                        continue;
                    }
                    
                    cmp.setName(paramNames[i]);
                    cmp.setType(ControllerMappingParameterTypeEnum.REQUEST_PARAM);
                    controllerMapping.getParameters().add(cmp);
				}
			}
		}
		registerMapping(url, controllerMapping);
	}

	abstract void registerMapping(String url, ControllerMapping controllerMapping);

	private String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
		// TODO Auto-generated method stub
		final Class<?>[] paramTypes = method.getParameterTypes();
		if(paramTypes == null || paramTypes.length == 0) {
			return null;
		}
		
		final Type[] types = new Type[paramTypes.length];
		for(int i=0 ;i < paramTypes.length ; i ++) {
			types[i] = Type.getType(paramTypes[i]);
		}
		
		final String[] parameterNames = new String[paramTypes.length];
		
		String className = clazz.getName();
		
		int lastDotIndex = className.lastIndexOf(".");  
		className = className.substring(lastDotIndex + 1) + ".class"; 
		InputStream is = clazz.getResourceAsStream(className);  
		try {
			
			ClassReader classReader = new ClassReader(is);
			classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override  
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {  
                    // 只处理指定的方法  
                    Type[] argumentTypes = Type.getArgumentTypes(desc);  
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {  
                        return null;  
                    }  
                    
                    return new MethodVisitor(Opcodes.ASM4) {  
                        @Override  
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {  
                            // 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this  
                            if (Modifier.isStatic(method.getModifiers())) {  
                                parameterNames[index] = name;  
                            }
                            else if (index > 0 && index <= parameterNames.length) {  
                                parameterNames[index - 1] = name;  
                            }  
                        }  
                    };  
                }		
			}, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return parameterNames;  
	}

	private String getMethodUrl(String baseUrl, String methodMappingUrl) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(256);
		url.append((baseUrl == null || baseUrl.trim().isEmpty()) ? "" : baseUrl.trim());
		
		if(methodMappingUrl != null || !methodMappingUrl.trim().isEmpty()) {
			String methodMapping = methodMappingUrl.trim();
			
			if(!methodMapping.startsWith("/")) {
				methodMapping = "/" + methodMapping;
			}
			
			if(url.toString().endsWith("/")) {
				url.setLength(url.length() - 1);
			}
			
			url.append(methodMapping);
			
		}
		return url.toString();
	}

	abstract String getMethodUrl(Method method);

	abstract String getHttpMethod();
}
