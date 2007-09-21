/*
 * Copyright (c) 2002-2007, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.formengine.service.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


/**
 * This abstract class defines the basis for file output processing
 */
public abstract class FileOutputProcessor extends OutputProcessor
{
    private String _strXmlFilePath;

    /**
     *
     */
    public FileOutputProcessor(  )
    {
    }

    /**
     *
     */
    protected void generateOutput( Object transactionObject )
    {
        try
        {
            File file = new File( getXmlFilePath(  ) );

            // return true to indicate a directory creation, false if directory exist
            File instanceDir = file.getParentFile(  );
            instanceDir.mkdirs(  );

            // return true to indicate a file creation, false if file exist
            //boolean bNewFile = file.createNewFile(  );
            FileOutputStream fout = new FileOutputStream( file );
            FileChannel channel = fout.getChannel(  );
            FileLock lock = channel.lock(  );

            this.write( fout, transactionObject );

            lock.release(  );
            fout.close(  );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }
    }

    /**
     * This method will actually write the data given by the input object into the output stream.
     * @param out the file outpustream
     * @param transactionObject the object to process
     */
    protected abstract void write( FileOutputStream out, Object transactionObject );

    /**
     * @return Returns the _strXmlFilePath.
     */
    public String getXmlFilePath(  )
    {
        return _strXmlFilePath;
    }

    /**
     * @param strXmlFilePath The _strXmlFilePath to set.
     */
    public void setXmlFilePath( String strXmlFilePath )
    {
        _strXmlFilePath = strXmlFilePath;
    }
}
