/*
 * Copyright (c) 2018 Uber Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package motif.intellij.validation.ir

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiAnnotationOwner
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiModifierListOwner
import motif.models.java.IrAnnotation
import motif.models.java.IrModifier
import java.util.*

interface IrUtil {

    fun PsiModifierListOwner.irModifiers(): Set<IrModifier> {
        return PsiModifier.MODIFIERS
                .filter { hasModifierProperty(it) }
                .map { IrModifier.valueOf(it.toUpperCase(Locale.getDefault())) }
                .toSet()
    }

    fun PsiAnnotationOwner.irAnnotations(project: Project): List<IrAnnotation> {
        return annotations.map { IntelliJAnnotation(project, it) }
    }

}