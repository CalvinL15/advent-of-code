import common.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

enum Pulse {
    LOW,
    HIGH
}

enum ModuleType {
    FLIPFLOP,
    CONJUNCTION,
    BROADCASTER
}

class Module {

    boolean isOn;
    String moduleName;
    ModuleType moduleType; // type b = broadcaster
    Pulse pulseToSend;
    HashMap<String, Pulse> inputs; // needed for conjunction module type
    List<String> outputs;

    public Module(String moduleName, ModuleType moduleType){
        this.isOn = false;
        this.moduleName = moduleName;
        this.moduleType = moduleType;
        this.inputs = new HashMap();
    }
}

public class Task20 {

    private static Pulse computeConjModulePulse(Module outputModule) {
        HashMap<String, Pulse> conjInputModulesInfo = outputModule.inputs;
        boolean isAllHighPulses = true;
        for (HashMap.Entry<String, Pulse> entry : conjInputModulesInfo.entrySet()) {
            if (entry.getValue() == Pulse.LOW) {
                isAllHighPulses = false;
                break;
            }
        }
        if (isAllHighPulses) {
            return Pulse.LOW;
        } return Pulse.HIGH;
    }

    // input specific
    // relevant modules for my input: &kz (broadcast to rx) and &sj, &qq, &ls, &bg (broadcast to &kz)
    // for rx to receive low pulse, &kz needs to remember high pulses for all its inputs
    private static long detectButtonPressed(HashMap<String, Module> modules){
        long whenSJHigh1 = 0;
        long whenQQHigh1 = 0;
        long whenLSHigh1 = 0;
        long whenBGHigh1 = 0;
        long whenSJHigh2 = 0;
        long whenQQHigh2 = 0;
        long whenLSHigh2 = 0;
        long whenBGHigh2 = 0;
        long buttonPressedCount = 0;
        while (whenBGHigh2 == 0 || whenLSHigh2 == 0 || whenQQHigh2 == 0 || whenSJHigh2 == 0) {
            buttonPressedCount++;
            Queue<Module> queue = new LinkedList();
            queue.add(modules.get("broadcaster"));
            while(!queue.isEmpty()){
                Module current = queue.poll();
                Pulse pulseToSend = current.moduleType == ModuleType.BROADCASTER ? Pulse.LOW : current.pulseToSend;
                List<String> outputs = current.outputs;
                for (String output : outputs) {
                    Module outputModule = modules.get(output);
                    if (outputModule == null) continue;
                    if (pulseToSend == Pulse.LOW) {
                        if (outputModule.moduleType == ModuleType.FLIPFLOP) {
                            outputModule.isOn = !outputModule.isOn ? true : false;
                            outputModule.pulseToSend = outputModule.isOn ? Pulse.HIGH : Pulse.LOW;
                        } else {
                            outputModule.inputs.put(current.moduleName, Pulse.LOW);
                            outputModule.pulseToSend = computeConjModulePulse(outputModule);
                            if (outputModule.moduleName.equals("sj") && outputModule.pulseToSend == Pulse.HIGH && whenSJHigh2 == 0){
                                if (whenSJHigh1 == 0)
                                    whenSJHigh1 = buttonPressedCount;
                                else whenSJHigh2 = buttonPressedCount;
                            }
                            else if (outputModule.moduleName.equals("qq") && outputModule.pulseToSend == Pulse.HIGH && whenQQHigh2 == 0){
                                if (whenQQHigh1 == 0)
                                    whenQQHigh1 = buttonPressedCount;
                                else whenQQHigh2 = buttonPressedCount;
                            } else if (outputModule.moduleName.equals("ls") && outputModule.pulseToSend == Pulse.HIGH && whenLSHigh2 == 0){
                                if (whenLSHigh1 == 0)
                                    whenLSHigh1 = buttonPressedCount;
                                else whenLSHigh2 = buttonPressedCount;
                            } else if (outputModule.moduleName.equals("bg") && outputModule.pulseToSend == Pulse.HIGH && whenBGHigh2 == 0){
                                if (whenBGHigh1 == 0)
                                    whenBGHigh1 = buttonPressedCount;
                                else whenBGHigh2 = buttonPressedCount;
                            }
                        }
                        queue.add(outputModule);
                    } else {
                        if (outputModule.moduleType == ModuleType.FLIPFLOP) {
                            // do nothing
                        } else {
                            outputModule.inputs.put(current.moduleName, Pulse.HIGH);
                            outputModule.pulseToSend = computeConjModulePulse(outputModule);
                            queue.add(outputModule);
                        }
                    }
                }
            }
        }
        long sjCycleLength = whenSJHigh2 - whenSJHigh1;
        long lsCycleLength = whenLSHigh2 - whenLSHigh1;
        long bgCycleLength = whenBGHigh2 - whenBGHigh1;
        long qqCycleLength = whenQQHigh2 - whenQQHigh1;
        Utils utils = new Utils();
        long curLcm = utils.lcm(sjCycleLength, lsCycleLength);
        curLcm = utils.lcm(curLcm, bgCycleLength);
        curLcm = utils.lcm(curLcm, qqCycleLength);
        long lcmMultiple = 0;
        long[] startingPoints = new long[]{whenSJHigh1, whenLSHigh1, whenBGHigh1, whenQQHigh1};
        for (long startingPoint : startingPoints) {
            long multiple = ((startingPoint + curLcm - 1) / curLcm) * curLcm;
            if (lcmMultiple < multiple) lcmMultiple = multiple;
        }
        System.out.println(lcmMultiple);
        return lcmMultiple;
    }
    private static long broadcastAndCountPulses(HashMap<String, Module> modules){
        long highPulsesCount = 0;
        long lowPulsesCount = 0;

        for (int i = 0; i<1000; i++){
            lowPulsesCount++;
            Queue<Module> queue = new LinkedList();
            queue.add(modules.get("broadcaster"));
            while(!queue.isEmpty()){
                Module current = queue.poll();
                Pulse pulseToSend = current.moduleType == ModuleType.BROADCASTER ? Pulse.LOW : current.pulseToSend;
                if (pulseToSend == Pulse.LOW) {
                    lowPulsesCount += current.outputs.size();
                } else {
                    highPulsesCount += current.outputs.size();
                }
                List<String> outputs = current.outputs;
                for (String output : outputs) {
                    Module outputModule = modules.get(output);
                    if (outputModule == null) continue;
                    if (pulseToSend == Pulse.LOW) {
                        if (outputModule.moduleType == ModuleType.FLIPFLOP) {
                            outputModule.isOn = !outputModule.isOn ? true : false;
                            outputModule.pulseToSend = outputModule.isOn ? Pulse.HIGH : Pulse.LOW;
                        } else {
                            outputModule.inputs.put(current.moduleName, Pulse.LOW);
                            outputModule.pulseToSend = computeConjModulePulse(outputModule);
                        }
                        queue.add(outputModule);
                    } else {
                        if (outputModule.moduleType == ModuleType.FLIPFLOP) {
                            // do nothing
                        } else {
                            outputModule.inputs.put(current.moduleName, Pulse.HIGH);
                            outputModule.pulseToSend = computeConjModulePulse(outputModule);
                            queue.add(outputModule);
                        }
                    }
                }
            }
        }

        return highPulsesCount*lowPulsesCount;
    }

    public static void main(String[] args){
        File input = new File("2023/input.txt");
        HashMap<String, Module> modules = new HashMap();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            // parse input
            while((line = reader.readLine()) != null){
                String[] moduleAndOutputs = line.split("->");
                String moduleName = moduleAndOutputs[0].substring(moduleAndOutputs[0].charAt(0) == 'b' ? 0 : 1, moduleAndOutputs[0].trim().length());
                Character operator = moduleAndOutputs[0].charAt(0);
                ModuleType moduleType = operator == '%' ? ModuleType.FLIPFLOP :
                        (operator == '&' ? ModuleType.CONJUNCTION : ModuleType.BROADCASTER);
                Module module = new Module(moduleName, moduleType);
                String[] outputs = moduleAndOutputs[1].trim().split(", ");
                module.outputs = Arrays.asList(outputs);
                modules.put(moduleName, module);
            }

            // initialize input modules for each module
            for (HashMap.Entry<String, Module> entry : modules.entrySet()) {
                String curModuleName = entry.getKey();
                Module curModule = entry.getValue();
                List<String> moduleOutput = curModule.outputs;
                for (String output : moduleOutput) {
                    if (modules.containsKey(output)) {
                        modules.get(output).inputs.put(curModuleName, Pulse.LOW);
                    }
                }
            }

            System.out.println("task A: " + broadcastAndCountPulses(modules));
            System.out.println("task B: " + detectButtonPressed(modules));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
