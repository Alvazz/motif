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
package motif.compiler.ir

import com.google.auto.common.MoreTypes
import com.google.common.base.Equivalence
import motif.models.java.IrClass
import motif.models.java.IrType
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class CompilerType(
        private val env: ProcessingEnvironment,
        val mirror: TypeMirror) : IrType {

    private val key: Equivalence.Wrapper<TypeMirror> = MoreTypes.equivalence().wrap(mirror)

    fun isInterface(): Boolean {
        return IrClass.Kind.INTERFACE == resolveClass()?.kind
    }

    override val qualifiedName: String by lazy {
        mirror.toString()
    }

    override val isVoid: Boolean by lazy {
        mirror.kind == TypeKind.VOID
    }

    override fun resolveClass(): IrClass? {
        if (mirror.kind != TypeKind.DECLARED) return null

        val declaredType: DeclaredType = mirror as DeclaredType

        return CompilerClass(env, declaredType)
    }

    override fun isAssignableTo(type: IrType): Boolean {
        return env.typeUtils.isAssignable(mirror, (type as CompilerType).mirror)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompilerType

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun toString(): String {
        return mirror.toString()
    }
}